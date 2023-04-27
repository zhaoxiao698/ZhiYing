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
import com.zhaoxiao.zhiying.entity.test.QuestionAnswer;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.entity.test.TestFtype;
import com.zhaoxiao.zhiying.entity.test.TranslationM;
import com.zhaoxiao.zhiying.entity.test.WritingM;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("test/getTestCollectionList")
    Call<Data<PageInfo<QuestionM>>> getTestCollectionList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                           @Query("account") String account, @Query("table") int table);

    @GET("test/getTestCollectionList")
    Call<Data<PageInfo<ListeningM>>> getTestCollectionList1(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                          @Query("account") String account, @Query("table") int table);

    @GET("test/getTestCollectionList")
    Call<Data<PageInfo<BankedM>>> getTestCollectionList2(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                          @Query("account") String account, @Query("table") int table);

    @GET("test/getTestCollectionList")
    Call<Data<PageInfo<MatchM>>> getTestCollectionList3(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                          @Query("account") String account, @Query("table") int table);

    @GET("test/getTestCollectionList")
    Call<Data<PageInfo<CarefulM>>> getTestCollectionList4(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                          @Query("account") String account, @Query("table") int table);

    @GET("test/getTestCollectionList")
    Call<Data<PageInfo<TranslationM>>> getTestCollectionList5(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                          @Query("account") String account, @Query("table") int table);

    @GET("test/getTestCollectionList")
    Call<Data<PageInfo<WritingM>>> getTestCollectionList6(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                          @Query("account") String account, @Query("table") int table);

    @GET("test/getTestCollectionList")
    Call<Data<PageInfo<ClozeM>>> getTestCollectionList7(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                          @Query("account") String account, @Query("table") int table);

    @GET("test/getTestCollectionList")
    Call<Data<PageInfo<NewM>>> getTestCollectionList8(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                          @Query("account") String account, @Query("table") int table);

    @GET("test/getQuestionById")
    Call<Data<QuestionM>> getQuestionById(@Query("questionId") int questionId, @Query("table") int table);

    @GET("test/getQuestionById")
    Call<Data<ListeningM>> getQuestionById1(@Query("questionId") int questionId, @Query("table") int table);

    @GET("test/getQuestionById")
    Call<Data<BankedM>> getQuestionById2(@Query("questionId") int questionId, @Query("table") int table);

    @GET("test/getQuestionById")
    Call<Data<MatchM>> getQuestionById3(@Query("questionId") int questionId, @Query("table") int table);

    @GET("test/getQuestionById")
    Call<Data<CarefulM>> getQuestionById4(@Query("questionId") int questionId, @Query("table") int table);

    @GET("test/getQuestionById")
    Call<Data<TranslationM>> getQuestionById5(@Query("questionId") int questionId, @Query("table") int table);

    @GET("test/getQuestionById")
    Call<Data<WritingM>> getQuestionById6(@Query("questionId") int questionId, @Query("table") int table);

    @GET("test/getQuestionById")
    Call<Data<ClozeM>> getQuestionById7(@Query("questionId") int questionId, @Query("table") int table);

    @GET("test/getQuestionById")
    Call<Data<NewM>> getQuestionById8(@Query("questionId") int questionId, @Query("table") int table);

    @GET("test/getTestHistoryList")
    Call<Data<PageInfo<QuestionM>>> getTestHistoryList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                          @Query("account") String account, @Query("table") int table);

    @GET("test/addTestRecord")
    Call<Data<Boolean>> addTestRecord(@Query("account") String account, @Query("questionId") int questionId, @Query("table") int table);

    @POST("test/saveAnswer")
    Call<Data<Boolean>> saveAnswer(@Body QuestionAnswer questionAnswer);

    @GET("test/getTestWrongList")
    Call<Data<PageInfo<QuestionM>>> getTestWrongList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                       @Query("account") String account, @Query("table") int table);
}
