package com.zhaoxiao.zhiying.entity.word;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WordSimple implements Serializable {
    @SerializedName("wordHead")
    private String wordHead;
    @SerializedName("wordId")
    private String wordId;
    @SerializedName("bookId")
    private String bookId;
    @SerializedName("picture")
    private String picture;
    @SerializedName("remMethod")
    private String remMethod;
    @SerializedName("ukphone")
    private String ukphone;
    @SerializedName("ukspeech")
    private String ukspeech;
    @SerializedName("usphone")
    private String usphone;
    @SerializedName("usspeech")
    private String usspeech;
    @SerializedName("sentences")
    private List<SentencesBean> sentences;
    @SerializedName("synos")
    private List<SynosBean> synos;
    @SerializedName("phrases")
    private List<PhrasesBean> phrases;
    @SerializedName("reals")
    private List<RealsBean> rels;
    @SerializedName("trans")
    private List<TransBean> trans;

    @SerializedName("proficiency")
    private int proficiency;
    @SerializedName("nextDayNum")
    private int nextDayNum;
    @SerializedName("collect")
    private boolean collect;

    public String getWordHead() {
        return wordHead;
    }

    public void setWordHead(String wordHead) {
        this.wordHead = wordHead;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRemMethod() {
        return remMethod;
    }

    public void setRemMethod(String remMethod) {
        this.remMethod = remMethod;
    }

    public String getUkphone() {
        return ukphone;
    }

    public void setUkphone(String ukphone) {
        this.ukphone = ukphone;
    }

    public String getUkspeech() {
        return ukspeech;
    }

    public void setUkspeech(String ukspeech) {
        this.ukspeech = ukspeech;
    }

    public String getUsphone() {
        return usphone;
    }

    public void setUsphone(String usphone) {
        this.usphone = usphone;
    }

    public String getUsspeech() {
        return usspeech;
    }

    public void setUsspeech(String usspeech) {
        this.usspeech = usspeech;
    }

    public List<SentencesBean> getSentences() {
        return sentences;
    }

    public void setSentences(List<SentencesBean> sentences) {
        this.sentences = sentences;
    }

    public List<SynosBean> getSynos() {
        return synos;
    }

    public void setSynos(List<SynosBean> synos) {
        this.synos = synos;
    }

    public List<PhrasesBean> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<PhrasesBean> phrases) {
        this.phrases = phrases;
    }

    public List<RealsBean> getRels() {
        return rels;
    }

    public void setRels(List<RealsBean> rels) {
        this.rels = rels;
    }

    public List<TransBean> getTrans() {
        return trans;
    }

    public void setTrans(List<TransBean> trans) {
        this.trans = trans;
    }

    public int getProficiency() {
        return proficiency;
    }

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

    public int getNextDayNum() {
        return nextDayNum;
    }

    public void setNextDayNum(int nextDayNum) {
        this.nextDayNum = nextDayNum;
    }

    public boolean getCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public static class SentencesBean implements Serializable {
        @SerializedName("scontent")
        private String scontent;
        @SerializedName("scn")
        private String scn;

        public String getScontent() {
            return scontent;
        }

        public void setScontent(String scontent) {
            this.scontent = scontent;
        }

        public String getScn() {
            return scn;
        }

        public void setScn(String scn) {
            this.scn = scn;
        }
    }

    public static class SynosBean implements Serializable {
        @SerializedName("pos")
        private String pos;
        @SerializedName("tran")
        private String tran;
        @SerializedName("hwds")
        private List<HwdsBean> hwds;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public String getTran() {
            return tran;
        }

        public void setTran(String tran) {
            this.tran = tran;
        }

        public List<HwdsBean> getHwds() {
            return hwds;
        }

        public void setHwds(List<HwdsBean> hwds) {
            this.hwds = hwds;
        }

        public static class HwdsBean implements Serializable {
            @SerializedName("w")
            private String w;

            public String getW() {
                return w;
            }

            public void setW(String w) {
                this.w = w;
            }
        }
    }

    public static class PhrasesBean implements Serializable {
        @SerializedName("pcontent")
        private String pcontent;
        @SerializedName("pcn")
        private String pcn;

        public String getPcontent() {
            return pcontent;
        }

        public void setPcontent(String pcontent) {
            this.pcontent = pcontent;
        }

        public String getPcn() {
            return pcn;
        }

        public void setPcn(String pcn) {
            this.pcn = pcn;
        }
    }

    public static class RealsBean implements Serializable {
        @SerializedName("pos")
        private String pos;
        @SerializedName("words")
        private List<WordsBean> words;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public List<WordsBean> getWords() {
            return words;
        }

        public void setWords(List<WordsBean> words) {
            this.words = words;
        }

        public static class WordsBean implements Serializable {
            @SerializedName("hwd")
            private String hwd;
            @SerializedName("tran")
            private String tran;

            public String getHwd() {
                return hwd;
            }

            public void setHwd(String hwd) {
                this.hwd = hwd;
            }

            public String getTran() {
                return tran;
            }

            public void setTran(String tran) {
                this.tran = tran;
            }
        }
    }

    public static class TransBean implements Serializable {
        @SerializedName("tranCn")
        private String tranCn;
        @SerializedName("descOther")
        private String descOther;
        @SerializedName("pos")
        private String pos;
        @SerializedName("descCn")
        private String descCn;
        @SerializedName("tranOther")
        private String tranOther;

        public String getTranCn() {
            return tranCn;
        }

        public void setTranCn(String tranCn) {
            this.tranCn = tranCn;
        }

        public String getDescOther() {
            return descOther;
        }

        public void setDescOther(String descOther) {
            this.descOther = descOther;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public String getDescCn() {
            return descCn;
        }

        public void setDescCn(String descCn) {
            this.descCn = descCn;
        }

        public String getTranOther() {
            return tranOther;
        }

        public void setTranOther(String tranOther) {
            this.tranOther = tranOther;
        }
    }
}
