package demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import android.app.Activity;
import android.util.Log;
import de.tavendo.autobahn.Wamp;
import de.tavendo.autobahn.Wamp.ConnectionHandler;
import de.tavendo.autobahn.WampConnection;
import de.tavendo.autobahn.WampOptions;
import de.tavendo.autobahn.secure.WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification;

public final class ApiServiceHelper<T> {

   public static interface RpcRequest<T> {

      Activity getActivity();

      String getHost();

      String getRequestUrl();

      Class<T> getReturnType();

      Object[] getParams();

   }

   public static interface PubSubListener<T> extends RpcRequest<T> {
      void onEvent(String topic, Object event);
   }

   private ApiServiceHelper() {
   }

   public static <T> T executeRequest(final RpcRequest<T> request) {
      final WampOptions wampOptions = new WampOptions();
      wampOptions.setReceiveTextMessagesRaw(true);
      final CountDownLatch methodCallLatch = new CountDownLatch(1);
      final CountDownLatch connectionLatch = new CountDownLatch(1);
      final AtomicReference<WampConnection> connectionRef = new AtomicReference<WampConnection>();
      final AtomicReference<T> resultRef = new AtomicReference<T>();
      request.getActivity().runOnUiThread(new Runnable() {
         @Override
         public void run() {// /
            final WampConnection connection = new WampConnection();
            connectionRef.set(connection);
            connectionLatch.countDown();
         }
      });
      if (waitRemote(connectionLatch)) {
         connectionRef.get().connect(request.getHost(),
               new ConnectionHandler() {
                  @Override
                  public void onOpen() {
                     connectionRef.get().call(request.getRequestUrl(),
                           request.getReturnType(), new Wamp.CallHandler() {
                              @Override
                              public void onResult(Object result) {
                                 T res = (T) result;
                                 resultRef.set(res);
                                 methodCallLatch.countDown();
                              }

                              @Override
                              public void onError(String error, String info) {
                                 Log.d(ApiServiceHelper.class.getSimpleName(),
                                       SystemHelper.buildStringNoSep(error,
                                             SystemHelper.SPACE, info));
                                 methodCallLatch.countDown();
                              }
                           }, request.getParams());

                  }

                  @Override
                  public void onClose(
                        /* int notif */WebSocketCloseNotification notif,
                        String reason) {
                     Log.d(ApiServiceHelper.class.getSimpleName(), SystemHelper
                           .buildStringNoSep(notif, SystemHelper.SPACE, reason));
                     methodCallLatch.countDown();

                  }
               }, wampOptions);
      }
      if (waitRemote(methodCallLatch)) {
         if (connectionRef.get() != null) {
            connectionRef.get().disconnect();
         }
      }
      return resultRef.get();

   }

   private static <T> boolean waitRemote(final CountDownLatch latch) {
      try {
         if (!latch.await(5000, TimeUnit.MILLISECONDS)) {
            return false;
         }
      } catch (InterruptedException e1) {
         Log.d(ApiServiceHelper.class.getSimpleName(),
               SystemHelper.buildStringNoSep(e1.getMessage()));
         return false;
      }
      return true;
   }

   public static <T> AtomicReference<WampConnection> subscribeTopic(
         final PubSubListener<T> request) {
      final WampOptions wampOptions = new WampOptions();
      wampOptions.setReceiveTextMessagesRaw(true);
      final CountDownLatch connectionLatch = new CountDownLatch(1);
      final AtomicReference<WampConnection> connectionRef = new AtomicReference<WampConnection>();

      request.getActivity().runOnUiThread(new Runnable() {
         @Override
         public void run() {// /
            final WampConnection connection = new WampConnection();
            connectionRef.set(connection);
            connectionLatch.countDown();
         }
      });
      if (waitRemote(connectionLatch)) {
         connectionRef.get().connect(request.getHost(),
               new ConnectionHandler() {
                  @Override
                  public void onOpen() {
                     connectionRef.get().subscribe(request.getRequestUrl(),
                           request.getReturnType(), new Wamp.EventHandler() {
                              @Override
                              public void onEvent(String topicUri, Object event) {
                                 request.onEvent(topicUri, event);
                              }
                           });
                  }

                  @Override
                  public void onClose(WebSocketCloseNotification notif,
                        String reason) {
                     Log.d(ApiServiceHelper.class.getSimpleName(), SystemHelper
                           .buildStringNoSep(notif, SystemHelper.SPACE, reason));

                  }
               }, wampOptions);
      }
      return connectionRef;
   }

   public static <T> void unsubscribeTopic(
         AtomicReference<WampConnection> connectionRef,
         final PubSubListener<T> request) {
      if (connectionRef.get() != null) {
         connectionRef.get().unsubscribe(request.getRequestUrl());
      }
   }

   public static <T> void unsubscribeAllTopics(
         AtomicReference<WampConnection> connectionRef) {
      if (connectionRef.get() != null) {
         connectionRef.get().unsubscribe();

      }
   }
}
