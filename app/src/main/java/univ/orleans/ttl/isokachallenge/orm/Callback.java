package univ.orleans.ttl.isokachallenge.orm;

import com.androidnetworking.error.ANError;

public interface Callback {
    void onResponse();
    void onError(ANError error);
}
