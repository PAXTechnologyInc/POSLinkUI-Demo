package com.paxus.pay.poslinkui.demo.entry.poslink;

import java.io.Serializable;

/**
 * Created by Peter.Du on 22/5/2025
 * //[{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}}]
 */
class MsgInfoWrapper {
    private String index;
    private MsgInfo msgInfo;

    public MsgInfoWrapper() {
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public MsgInfo getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(MsgInfo msgInfo) {
        this.msgInfo = msgInfo;
    }


    public static class MsgInfo implements Serializable {
        private String msg1;
        private String msg2;

        public MsgInfo(String msg1, String msg2) {
            this.msg1 = msg1;
            this.msg2 = msg2;
        }

        public String getMsg2() {
            return msg2;
        }

        public void setMsg2(String msg2) {
            this.msg2 = msg2;
        }

        public String getMsg1() {
            return msg1;
        }

        public void setMsg1(String msg1) {
            this.msg1 = msg1;
        }
    }
}



