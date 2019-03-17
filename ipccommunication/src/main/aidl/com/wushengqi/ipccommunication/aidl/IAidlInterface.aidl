// IAidlInterface.aidl
package com.wushengqi.ipccommunication.aidl;

//如果使用了其它类，要手动引入
import com.wushengqi.ipccommunication.aidl.Book;
import com.wushengqi.ipccommunication.aidl.AIDLCallback;
// Declare any non-default types here with import statements

interface IAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void addBookInOut(inout Book book);

    void registerAIDLCallback(AIDLCallback aidlCallback);

    void unregisterAIDLCallback(AIDLCallback aidlCallback);
}
