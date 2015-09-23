package com.liuxuecanada.liuxuecanada.Utils;

public interface AsyncResponse {

    void onTaskComplete(Object output);

    void onTaskStart();
}
