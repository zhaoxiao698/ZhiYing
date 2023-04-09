package com.zhaoxiao.zhiying.api;

import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.study.ArticleDetail;
import com.zhaoxiao.zhiying.entity.study.Banner;
import com.zhaoxiao.zhiying.entity.study.Channel;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.Ftype;
import com.zhaoxiao.zhiying.entity.study.Hot;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.study.Recent;
import com.zhaoxiao.zhiying.entity.test.BankedM;
import com.zhaoxiao.zhiying.entity.test.CarefulM;
import com.zhaoxiao.zhiying.entity.test.ClozeM;
import com.zhaoxiao.zhiying.entity.test.ListeningM;
import com.zhaoxiao.zhiying.entity.test.MatchM;
import com.zhaoxiao.zhiying.entity.test.NewM;
import com.zhaoxiao.zhiying.entity.test.TestFtype;
import com.zhaoxiao.zhiying.entity.test.TranslationM;
import com.zhaoxiao.zhiying.entity.test.WritingM;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TestService {
    @GET("test/getTestFtypeList")
    Call<Data<List<TestFtype>>> getTestFtypeList(@Query("questionBankId") int questionBankId);

    @GET("test/getListeningList")
    Call<Data<List<ListeningM>>> getListeningList(@Query("random") boolean random, @Query("limitNum") int limitNum,
                                                  @Query("questionBankId") int questionBankId, @Query("num") int num,
                                                  @Query("type") int type);

    @GET("test/getBankedList")
    Call<Data<List<BankedM>>> getBankedList(@Query("random") boolean random, @Query("limitNum") int limitNum,
                                            @Query("questionBankId") int questionBankId);

    @GET("test/getMatchList")
    Call<Data<List<MatchM>>> getMatchList(@Query("random") boolean random, @Query("limitNum") int limitNum,
                                          @Query("questionBankId") int questionBankId);

    @GET("test/getCarefulList")
    Call<Data<List<CarefulM>>> getCarefulList(@Query("random") boolean random, @Query("limitNum") int limitNum,
                                              @Query("questionBankId") int questionBankId);

    @GET("test/getTranslationList")
    Call<Data<List<TranslationM>>> getTranslationList(@Query("random") boolean random, @Query("limitNum") int limitNum,
                                                      @Query("questionBankId") int questionBankId);

    @GET("test/getWritingList")
    Call<Data<List<WritingM>>> getWritingList(@Query("random") boolean random, @Query("limitNum") int limitNum,
                                              @Query("questionBankId") int questionBankId, @Query("type") int type);

    @GET("test/getClozeList")
    Call<Data<List<ClozeM>>> getClozeList(@Query("random") boolean random, @Query("limitNum") int limitNum,
                                          @Query("questionBankId") int questionBankId);

    @GET("test/getNewList")
    Call<Data<List<NewM>>> getNewList(@Query("random") boolean random, @Query("limitNum") int limitNum,
                                      @Query("questionBankId") int questionBankId, @Query("type") int type);
}
