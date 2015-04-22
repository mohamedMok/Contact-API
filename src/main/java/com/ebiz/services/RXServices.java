package com.ebiz.services;

import com.ebiz.connection.ESConnection;
import com.ebiz.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by ebiz on 10/02/2015.
 */
public class RXServices {

    public RXServices(){

    }

    public void defineObservable(List<User> listUser){
        //
        Observable<Observable<List<BulkResponse>>> userObservable = Observable.from(listUser)
                .map(new Func1<User, Observable<IndexRequestBuilder>>() {
                    @Override
                    public Observable<IndexRequestBuilder> call(User u) {
                        ObjectMapper mapper = new ObjectMapper();
                        IndexRequestBuilder index = ESConnection.getInstance().getDB().prepareIndex("contact", "users");
                        String json = "";
                        try {
                            json = mapper.writeValueAsString(u);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        index.setSource(json);
                        Observable<IndexRequestBuilder> indexRequestBuilderObservable = Observable.just(index);
                        return indexRequestBuilderObservable;
                    }
                })
                .buffer(100)
                .map(new Func1<List<Observable<IndexRequestBuilder>>, Observable<BulkRequestBuilder>>() {
                    @Override
                    public Observable<BulkRequestBuilder> call(List<Observable<IndexRequestBuilder>> observables) {

                        final BulkRequestBuilder bulkRequest = ESConnection.getInstance().getDB().prepareBulk();

                        for (Observable<IndexRequestBuilder> i : observables) {
                            i.subscribe(new Action1<IndexRequestBuilder>() {
                                @Override
                                public void call(IndexRequestBuilder indexRequestBuilder) {
                                    bulkRequest.add(indexRequestBuilder);
                                }
                            });
                        }
                        Observable<BulkRequestBuilder> bulkRequestBuilderObservable = Observable.just(bulkRequest);
                        return bulkRequestBuilderObservable;

                    }
                })
                .map(new Func1<Observable<BulkRequestBuilder>, Observable<List<BulkResponse>>>() {
                    @Override
                    public Observable<List<BulkResponse>> call(Observable<BulkRequestBuilder> bulkRequestBuilderObservable) {
                        final List<BulkResponse> listbulkResponse = new ArrayList<BulkResponse>();
                        bulkRequestBuilderObservable.subscribe(new Action1<BulkRequestBuilder>() {
                            @Override
                            public void call(BulkRequestBuilder bulkRequestBuilder) {
                                System.out.println("Size : " + bulkRequestBuilder.numberOfActions());
                                BulkResponse b = bulkRequestBuilder.execute().actionGet();
                                listbulkResponse.add(b);
                            }
                        });
                        Observable<List<BulkResponse>> bulkRequest = Observable.just(listbulkResponse);
                        return bulkRequest;
                    }
                });
        userObservable.subscribe(new Action1<Observable<List<BulkResponse>>>() {
            @Override
            public void call(Observable<List<BulkResponse>> listObservable) {
                System.out.println("On next");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("Error " + throwable);
            }
        }, new Action0() {
            @Override
            public void call() {
                System.out.println("The flow ends");
            }
        });
    }
}
