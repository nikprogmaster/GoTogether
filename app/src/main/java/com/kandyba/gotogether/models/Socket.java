package com.kandyba.gotogether.models;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kandyba.gotogether.BuildConfig;
import com.kandyba.gotogether.models.general.SocketMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.ws.RealWebSocket;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;

/**
 * Веб-сокет, основанный на OkHttp3
 */

public class Socket {

    private final static String TAG = Socket.class.getSimpleName();
    private final static String CLOSE_REASON = "End of session";

    private final static String HASH_KEY = "hash";
    private final static String USER_ID_KEY = "userId";
    private final static String ID_KEY = "id";
    private final static String DELIVERED_KEY = "delivered";
    private final static String DIALOG_ID_KEY = "dialogId";
    private final static String MESSAGE_KEY = "message";
    private final static String TIME_KEY = "time";

    /**
     * Состояния сокета
     */
    public enum State {
        CLOSED, CLOSING, CONNECT_ERROR, RECONNECT_ATTEMPT, RECONNECTING, OPENING, OPEN
    }

    private static final HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.HEADERS : HttpLoggingInterceptor.Level.NONE);

    private static final OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .addInterceptor(logging);

    public static class Builder {

        private final Request.Builder request;

        private Builder(Request.Builder request) {
            this.request = request;
        }

        /**
         * Создать {@link Builder} с определенным URL
         */
        public static Builder with(@NonNull String url) {
            if (!url.regionMatches(true, 0, "ws:", 0, 3)
                    && !url.regionMatches(true, 0, "wss:", 0, 4))
                throw new IllegalArgumentException("web socket url must start with ws or wss, passed url is " + url);

            return new Builder(new Request.Builder().url(url));
        }

        /** Добавление заголовка в запрос на открытие сокета */
        public Builder addHeader(@NonNull String name, @NonNull String value) {
            request.addHeader(name, value);
            return this;
        }

        /** Создать {@link Socket} */
        public Socket build() {
            return new Socket(request.build());
        }
    }


    private static State state;

    private static Request request;

    private static RealWebSocket realWebSocket;

    private static OnStateChangeListener onChangeStateListener;

    private static UniversalListener universalListener;

    private static boolean skipOnFailure;

    private Socket(Request request) {
        Socket.request = request;
        state = State.CLOSED;
        skipOnFailure = false;
    }

    /**
     * Открыть соединение сокета
     */
    public Socket connect() {
        if (httpClient == null) {
            throw new IllegalStateException("Make sure to use Socket.Builder before using Socket#connect.");
        }
        if (realWebSocket == null) {
            realWebSocket = (RealWebSocket) httpClient.build().newWebSocket(request, webSocketListener);
            changeState(State.OPENING);
        } else if (state == State.CLOSED) {
            realWebSocket.connect(httpClient.build());
            changeState(State.OPENING);
        }
        return this;
    }

    /**
     * Установить универсальный слушатель
     *
     * @param listener слушатель
     */
    public Socket setUniversalListener(@NonNull UniversalListener listener) {
        universalListener = listener;
        return this;
    }


    /**
     * Отправить сообщение через сокет
     *
     * @param socketMessage сообщение
     * @return true если сообщение отправлено, иначе false
     */
    public boolean send(@NonNull SocketMessage socketMessage) {
        try {
            if (state == State.CLOSED) {
                reconnect();
                return false;
            }
            JSONObject body = new JSONObject();
            body.put(HASH_KEY, hashMessage(socketMessage.hashCode()).toString());
            body.put(USER_ID_KEY, socketMessage.getUserId());
            body.put(DIALOG_ID_KEY, socketMessage.getDialogId());
            body.put(MESSAGE_KEY, socketMessage.getMessage());
            body.put(TIME_KEY, socketMessage.getTime());
            Log.v(TAG, "Try to send data " + body.toString());
            return realWebSocket.send(body.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Try to send data with wrong JSON format, data: " + socketMessage);
        }
        return false;
    }

    /**
     * Установить слушатель состояния сокета
     *
     * @param listener слушатель
     */
    public Socket setOnChangeStateListener(@NonNull OnStateChangeListener listener) {
        onChangeStateListener = listener;
        return this;
    }

    /**
     * Очистить все лисененры
     */
    public void clearListeners() {
        universalListener = null;
        onChangeStateListener = null;
    }

    /**
     * Закрыть сокет
     */
    public void close() {
        if (realWebSocket != null) {
            Log.i("close socket", "закрыт вроде");
            boolean isClosed = realWebSocket.close(1000, CLOSE_REASON);
            Log.i("isClosed", Boolean.toString(isClosed));
        }
    }


    /**
     * Завершить сокет соединение
     */
    public void terminate() {
        skipOnFailure = true; // skip onFailure callback
        if (realWebSocket != null) {
            realWebSocket.cancel(); // close connection
            realWebSocket = null; // clear socket object
        }
    }


    /**
     * Получить статус сокета
     *
     * @return {@link State}
     */
    public State getState() {
        return state;
    }

    /**
     * Хэщировать сообщение
     *
     * @param hashCode хэш-код сообщения
     * @return хэшированное значение с типом {@link Long}
     */
    private Long hashMessage(Integer hashCode) {
        return hashCode + Calendar.getInstance().getTimeInMillis();
    }

    /**
     * Изменить состояние сокета
     *
     * @param newState новое состояние
     */
    private void changeState(State newState) {
        state = newState;
        if (onChangeStateListener != null) {
            onChangeStateListener.onChange(Socket.this, state);
        }
    }

    /**
     * Попытка переподключиться
     */
    private void reconnect() {
        Log.i("Connect to Socket", "from reconnect");
        if (state != State.CONNECT_ERROR)
            return;

        changeState(State.RECONNECT_ATTEMPT);

        if (realWebSocket != null) {
            close();
            realWebSocket = null;
        }

        changeState(State.RECONNECTING);
        connect();

    }

    private final WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            Log.v(TAG, "Socket has been opened successfully.");
            universalListener.onOpen();
            changeState(State.OPEN);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            Log.v(TAG, "Получено новое сообщение" + text);

            try {
                JSONObject response = new JSONObject(text);
                String id = response.getString(ID_KEY);
                String hash = response.getString(HASH_KEY);
                String userId = response.getString(USER_ID_KEY);
                String dialogId = response.getString(DIALOG_ID_KEY);
                String messageText = response.getString(MESSAGE_KEY);
                Long time = response.getLong(TIME_KEY);
                Boolean delivered = response.getBoolean(DELIVERED_KEY);
                SocketMessage socketMessage = new SocketMessage(id, hash, userId, dialogId, messageText, time, delivered);
                universalListener.onMessage(socketMessage);
            } catch (JSONException e) {
                Log.e(TAG, "Неизвестный формат сообщения");
            }
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
            Log.i("OnMessage", "ByteString");
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, @NonNull String reason) {
            Log.v(TAG, "Close request from server with reason '" + reason + "'");
            changeState(State.CLOSING);
            webSocket.close(1000, reason);
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            Log.v(TAG, "Socket connection closed with reason '" + reason + "'");
            changeState(State.CLOSED);
            universalListener.onClosed();
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
            if (!skipOnFailure) {
                skipOnFailure = false; // reset flag
                universalListener.onFailure();
                changeState(State.CONNECT_ERROR);
                reconnect();
            }
        }
    };

    public abstract static class UniversalListener {
        public abstract void onOpen();

        public abstract void onMessage(SocketMessage socketMessage);

        public abstract void onClosed();

        public abstract void onFailure();
    }
    public abstract static class OnStateChangeListener {

        public abstract void onChange(State status);

        private void onChange(Socket socket, final State status) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onChange(status);
                }
            });
        }
    }

}