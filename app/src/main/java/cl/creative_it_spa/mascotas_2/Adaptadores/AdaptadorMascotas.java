package cl.creative_it_spa.mascotas_2.Adaptadores;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cl.creative_it_spa.mascotas_2.ConfigurarCuenta;
import cl.creative_it_spa.mascotas_2.Fragments.FDetalleMascotas;
import cl.creative_it_spa.mascotas_2.POJOs.LikeMascota;
import cl.creative_it_spa.mascotas_2.POJOs.ListaMascotas;
import cl.creative_it_spa.mascotas_2.POJOs.OrdenarMascotasTiempo;
import cl.creative_it_spa.mascotas_2.R;
import cl.creative_it_spa.mascotas_2.restApi.Adapter.RestApiAdapter;
import cl.creative_it_spa.mascotas_2.restApi.ConstantesRestApi;
import cl.creative_it_spa.mascotas_2.restApi.EndPointsApi;
import cl.creative_it_spa.mascotas_2.restApi.modeloRespuestaJsonInstagram.DarLikeJson;
import cl.creative_it_spa.mascotas_2.restApi.modeloRespuestaJsonInstagram.MascotaJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rodrigo on 13-06-2017.
 */

public class AdaptadorMascotas extends RecyclerView.Adapter<AdaptadorMascotas.MascotasViewHolder>{

    ArrayList<ListaMascotas> mascotaOrdenada, mascotasDesordenadas;
    Activity activity;
    String idMascotaSeleccionada="";
    String idMascotaLike="";
    ArrayList<LikeMascota> likeMascotas;

    public AdaptadorMascotas(ArrayList<ListaMascotas> mascotas, Activity activity) {
        this.mascotasDesordenadas = mascotas;
        this.activity = activity;
        ordenar();
    }

    private void ordenar(){
        OrdenarMascotasTiempo ordenarMascotasTiempo= new OrdenarMascotasTiempo(mascotasDesordenadas);
        mascotaOrdenada = ordenarMascotasTiempo.ordenar();
    }

    @Override
    public MascotasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mascotas, parent, false);
        return new MascotasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MascotasViewHolder holder_de_mascotas, final int position) {
        final ListaMascotas mascotax= mascotaOrdenada.get(position);

        Picasso.with(activity)
                .load(mascotax.getFoto_mascota())
                .placeholder(R.drawable.default_dog)
                .into(holder_de_mascotas.img_foto_mascota);

        holder_de_mascotas.tv_nombre_mascota.setText(mascotax.getNombre_mascota());
        holder_de_mascotas.tv_puntos_mascota.setText("" + mascotax.getPuntaje_mascota());

       if (idMascotaSeleccionada == ""){
           idMascotaSeleccionada = mascotaOrdenada.get(0).getId();
       }
        if(idMascotaSeleccionada == mascotax.getId()){
            holder_de_mascotas.cvMascotas.setBackgroundColor(activity.getResources().getColor(R.color.colorSeleccion));
        }else{
            holder_de_mascotas.cvMascotas.setBackgroundColor(activity.getResources().getColor(R.color.colorBlanco));
        }


        holder_de_mascotas.img_hueso_blanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);

                idMascotaLike=mascotax.getId();







                //darLike ********************************

                RestApiAdapter restApiAdapter2=new RestApiAdapter();
                Gson gsonLike = restApiAdapter2.construyrDeserializadorLikes();
                EndPointsApi endPointsApi = restApiAdapter2.EstablecerConexionRestApiInstagram(gsonLike);
                Call<DarLikeJson> likeJsonCall2= endPointsApi.sendLike2(ConstantesRestApi.KEY_SEND_LIKE_1 +
                        idMascotaLike +
                        ConstantesRestApi.KEY_SEND_LIKE_2, ConstantesRestApi.ACCESS_TOKEN);

                likeJsonCall2.enqueue(new Callback<DarLikeJson>() {
                    @Override
                    public void onResponse(Call<DarLikeJson> call, Response<DarLikeJson> response) {
                        DarLikeJson darLikeJson= response.body();
                        likeMascotas = darLikeJson.getLikes_mascota();

                        if (likeMascotas.size()>0){








                            //Actualizar número de Likes *********************************************

                            RestApiAdapter restApiAdapter3=new RestApiAdapter();
                            Gson gsonMediaRecent2 = restApiAdapter3.construyeGsonDeserializadorMediaRecent();
                            EndPointsApi endPointsApi2 = restApiAdapter3.EstablecerConexionRestApiInstagram(gsonMediaRecent2);
                            Call<MascotaJson> mascotaJsonCall=endPointsApi2.getRecentMediaByUser(
                                    ConstantesRestApi.KEY_GET_RECENT_MEDIA_USER_1 +
                                            ConfigurarCuenta.id_usuario_cuenta +
                                            ConstantesRestApi.KEY_GET_RECENT_MEDIA_USER_2 +
                                            ConstantesRestApi.KEY_ACCESS_TOKEN +
                                            ConstantesRestApi.ACCESS_TOKEN );

                            mascotaJsonCall.enqueue(new Callback<MascotaJson>() {
                                @Override
                                public void onResponse(Call<MascotaJson> call, Response<MascotaJson> response) {
                                    MascotaJson mascotaJson = response.body();
                                    ArrayList<ListaMascotas> listaActualizadaMascotas;
                                    listaActualizadaMascotas= mascotaJson.getListaJsonMascotas();

                                    int puntajeActualizado=0;
                                    for (int i = 0; i < listaActualizadaMascotas.size(); i++) {
                                        ListaMascotas mascotaAnalizada = listaActualizadaMascotas.get(i);

                                        if (idMascotaLike.equals(mascotaAnalizada.getId())){
                                            puntajeActualizado= mascotaAnalizada.getPuntaje_mascota();
                                            break;
                                        }
                                    }

                                    for (int j = 0; j < mascotaOrdenada.size(); j++) {
                                        ListaMascotas mascotaAnalizada = mascotaOrdenada.get(j);
                                        if (idMascotaLike.equals(mascotaAnalizada.getId())){
                                            mascotaOrdenada.get(j).setPuntaje_mascota(puntajeActualizado);
                                            notifyDataSetChanged();
                                            Toast.makeText(activity, "OK, haz dado like a " +
                                                    mascotaOrdenada.get(j).getNombre_mascota(), Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MascotaJson> call, Throwable t) {
                                    //implementar algo por si falla
                                    Log.e("FALLO LA CONEXION", t.toString());
                                }
                            });









                        } else {
                    Toast.makeText(activity, "FALLO LA CONEXION", Toast.LENGTH_SHORT).show();
                }
                    }

                    @Override
                    public void onFailure(Call<DarLikeJson> call, Throwable t) {
                        Log.e("FALLO LA CONEXION *****", t.toString());
                    }
                });

            }
        });

        holder_de_mascotas.img_foto_mascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);

                idMascotaSeleccionada = mascotax.getId();


                FDetalleMascotas fragmento= new FDetalleMascotas(idMascotaSeleccionada);
                FragmentTransaction transaccion= ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                transaccion.replace(R.id.ly_mascota_seleccionada, fragmento);
                transaccion.addToBackStack(null);
                transaccion.commit();

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mascotaOrdenada.size();
    }





    public static class MascotasViewHolder extends RecyclerView.ViewHolder{

        ImageView img_foto_mascota, img_hueso_color;
        TextView tv_nombre_mascota;
        TextView tv_puntos_mascota;
        ImageButton img_hueso_blanco;
        CardView cvMascotas;

        public MascotasViewHolder(View itemView) {
            super(itemView);

            img_foto_mascota=(ImageView) itemView.findViewById(R.id.img_foto_mascota);
            tv_nombre_mascota=(TextView) itemView.findViewById(R.id.tv_nombre_mascota);
            tv_puntos_mascota=(TextView) itemView.findViewById(R.id.tv_puntos_mascota);
            img_hueso_blanco=(ImageButton) itemView.findViewById(R.id.img_hueso_blanco);
            img_hueso_color=(ImageView) itemView.findViewById(R.id.img_hueso_color);

            cvMascotas=(CardView) itemView.findViewById(R.id.cvMascotas);
        }
    }
}
