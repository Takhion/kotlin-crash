import android.os.Handler
import android.os.Looper
import me.eugeniomarletti.redux.createStore
import me.eugeniomarletti.redux.reducer

fun crashRepro() = Runnable {
    mainThread {
        val subscription = store.subscribe {}
        post(subscription::unsubscribe)
    }
}

val mainHandler = Handler(Looper.getMainLooper())
val store = createStore(reducer({}, { action, state -> }))

inline fun mainThread(crossinline action: () -> Unit) {
    if (Looper.myLooper() === Looper.getMainLooper()) {
        action()
    } else {
        post(action)
    }
}

inline fun post(crossinline action: () -> Unit) {
    mainHandler.post { action() }
}
