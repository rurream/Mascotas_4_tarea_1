package cl.creative_it_spa.mascotas_2;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

import cl.creative_it_spa.mascotas_2.Adaptadores.PageAdapterMascotas;
import cl.creative_it_spa.mascotas_2.Fragments.FDetalleMascotas;
import cl.creative_it_spa.mascotas_2.Fragments.FListaMascotas;

public class MainActivity extends AppCompatActivity {

    private Toolbar barra1, barra2;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        barra1 = (Toolbar) findViewById(R.id.barra_sup);
        tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        viewPager=(ViewPager) findViewById(R.id.viewPager);
        setSupportActionBar(barra1);
        barra1.setTitleTextColor(getResources().getColor(R.color.colorBlanco));
        barra2=(Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(barra1);

        setUpViewPager();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_view, menu);
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.m_about:
                Intent i = new Intent(this, AcernaDe.class);
                startActivity(i);
                break;
            case R.id.m_contacto:
                Intent u = new Intent(this, Contacto.class);
                startActivity(u);
                break;
            case R.id.m_configurar_cuenta:
                Intent v = new Intent(this, ConfigurarCuenta.class);
                startActivity(v);
                break;
            case R.id.mTop_Five:
                Intent llamado=new Intent(getApplicationContext(), Marcador.class);
                startActivity(llamado);
                break;
            case R.id.m_recibir_notificaciones:
                Intent notificaciones=new Intent(getApplicationContext(), RecibirNotificaciones.class);
                startActivity(notificaciones);
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    private ArrayList<Fragment> agregarFragments(){
        ArrayList<Fragment> fragments= new ArrayList<>();

        fragments.add(new FListaMascotas());
        fragments.add(new FDetalleMascotas("1"));

        return fragments;
    }

    private void setUpViewPager(){
        viewPager.setAdapter(new PageAdapterMascotas(getSupportFragmentManager(), agregarFragments()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_action_name);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_dog_negro_lleno);
    }
}
