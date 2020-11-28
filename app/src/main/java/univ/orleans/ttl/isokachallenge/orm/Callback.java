package univ.orleans.ttl.isokachallenge.orm;

import com.androidnetworking.error.ANError;

/**
 * Interface permettant de remplacer les JSONObjectRequestListener
 * Dans les requêtes Get du RequestWrapper (Ce callback est executé après
 * le callback du JSONObjectRequestListener)
 */
public interface Callback {
    void onResponse();
    void onError(ANError error);
}
