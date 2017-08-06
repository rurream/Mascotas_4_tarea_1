package cl.creative_it_spa.mascotas_2.restApi;

import cl.creative_it_spa.mascotas_2.ConfigurarCuenta;
import cl.creative_it_spa.mascotas_2.restApi.modeloRespuestaJsonInstagram.DarLikeJson;
import cl.creative_it_spa.mascotas_2.restApi.modeloRespuestaJsonInstagram.MascotaJson;
import cl.creative_it_spa.mascotas_2.restApi.modeloRespuestaJsonInstagram.UsuarioJson;
import cl.creative_it_spa.mascotas_2.restApi.modeloRespuestaJsonInstagram.UsuarioResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Rodrigo on 19-07-2017.
 */

public interface EndPointsApi {

    @GET
    Call<UsuarioJson> getDataByUserName(@Url String url);

    @GET
    Call<MascotaJson> getRecentMediaByUser(@Url String url);


    @FormUrlEncoded
    @POST()
    Call<DarLikeJson> sendLike2(@Url String url, @Field("access_token") String token);


    @FormUrlEncoded
    @POST(ConstantesRestApi.KEY_POST_ID_TOKEN)
    Call<UsuarioResponse> registrarTokenID(@Field("token") String token);


    @FormUrlEncoded
    @POST(ConstantesRestApi.KEY_POST_ID_TOKEN_ID_USUARIO_INSTAGRAM)
    Call<UsuarioResponse> registrarTokenIDTarea(@Field("token") String token,
                                                @Field("id_usuario_instagram") String id_usuario_instagram);
}
