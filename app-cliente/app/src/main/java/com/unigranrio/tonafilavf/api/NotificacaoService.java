package com.unigranrio.tonafilavf.api;

import com.unigranrio.tonafilavf.model.NotificacaoDados;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificacaoService {

    @Headers({
            "Authorization:key=AAAAXrF1iNM:APA91bHrUEeaiteVg99pttFVcvXkdL_S4FB6WFlObUc09TYpmOM_JaS5ka6mH0jZuGxqFEdypj4nVMoODvPxUD3kV4vJuAVxD0zHZKD-VibupadVKlDM-s8rp52BHmR8ELehbbkNTPj6",
            "Content-Type:application/json"
    })
    @POST("send")
    Call<NotificacaoDados> salvarNotificacao(@Body NotificacaoDados notificacaoDados);

}