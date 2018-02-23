//
//package com.m520it.alipay;
//
//import android.util.Log;
//
//import java.util.ArrayList;
//
//import static com.m520it.alipay.BaseService.TAG;
//
///**
// * Created by 520 on 2016/11/28.
// */
//public interface IMessenger extends android.os.IInterface {
//
//    /**
//     * Local-side IPC implementation stub class.
//     */
//    public static abstract class Stub extends android.os.Binder implements IMessenger {
//
//        private static final String DESCRIPTOR = "com.m520it.alipay.IAlipay";
//        ArrayList<String> companyId = new ArrayList<String>();
//
//        /**
//         * Construct the stub at attach it to the interface.
//         */
//        public Stub() {
//
//            this.attachInterface(this, DESCRIPTOR);
//
//            companyId.add("com.bing.lan.lanbing");
//            companyId.add("com.m520it.paopaocar1");
//        }
//
//        /**
//         * Cast an IBinder object into an com.m520it.alipay.IAlipay interface,
//         * generating a proxy if needed.
//         */
//
//        public static IMessenger asInterface(android.os.IBinder obj) {
//            if ((obj == null)) {
//                return null;
//            }
//            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
//            if (((iin != null) && (iin instanceof IMessenger))) {
//                return ((IMessenger) iin);
//            }
//            return new Stub.Proxy(obj);
//        }
//
//        @Override
//        public android.os.IBinder asBinder() {
//            return this;
//        }
//
//        @Override
//        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
//            switch (code) {
//                case INTERFACE_TRANSACTION: {
//                    reply.writeString(DESCRIPTOR);
//                    return true;
//                }
//                case TRANSACTION_callSafePay: {
//                    data.enforceInterface(DESCRIPTOR);
//                    String _arg0;
//                    _arg0 = data.readString();
//                    String _arg1;
//                    _arg1 = data.readString();
//                    String _arg2;
//                    _arg2 = data.readString();
//                    double _arg3;
//                    _arg3 = data.readDouble();
//                    long _arg4;
//                    _arg4 = data.readLong();
//
//                    int _result = this.callSafePay(_arg0, _arg1, _arg2, _arg3, _arg4);
//                    reply.writeNoException();
//                    reply.writeInt(_result);
//
//                    //权限验证
//                    if (companyId.contains(_arg0)) {
//                        return true;
//                    }
//                    Log.e(TAG, "请先在支付宝注册");
//
//                    return false;
//                }
//            }
//            return super.onTransact(code, data, reply, flags);
//        }
//
//        private static class Proxy implements IMessenger {
//
//            private android.os.IBinder mRemote;
//
//            Proxy(android.os.IBinder remote) {
//                mRemote = remote;
//            }
//
//            @Override
//            public android.os.IBinder asBinder() {
//                return mRemote;
//            }
//
//            public String getInterfaceDescriptor() {
//                return DESCRIPTOR;
//            }
//
//            @Override
//            public int callSafePay(String companyId, String account, String pwd, double money, long currTimeMills) throws android.os.RemoteException {
//                android.os.Parcel _data = android.os.Parcel.obtain();
//                android.os.Parcel _reply = android.os.Parcel.obtain();
//                int _result;
//                try {
//                    _data.writeInterfaceToken(DESCRIPTOR);
//                    _data.writeString(companyId);
//                    _data.writeString(account);
//                    _data.writeString(pwd);
//                    _data.writeDouble(money);
//                    _data.writeLong(currTimeMills);
//                    mRemote.transact(Stub.TRANSACTION_callSafePay, _data, _reply, 0);
//                    _reply.readException();
//                    _result = _reply.readInt();
//                } finally {
//                    _reply.recycle();
//                    _data.recycle();
//                }
//                return _result;
//            }
//        }
//
//        static final int TRANSACTION_callSafePay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
//    }
//
//    public int callSafePay(String companyId, String account, String pwd, double money, long currTimeMills) throws android.os.RemoteException;
//}
