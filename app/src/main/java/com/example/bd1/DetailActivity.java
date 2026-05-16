package com.example.bd1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Chapitre 2 : Activation du bouton de retour dans l'action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Détails du produit");
        }

        TextView tvTitre = findViewById(R.id.tv_detail_titre);
        TextView tvInfos = findViewById(R.id.tv_detail_infos);
        android.widget.ImageView ivCover = findViewById(R.id.iv_detail_cover);
        LinearLayout llArtistes = findViewById(R.id.ll_detail_artistes);

        // Chapitre 2 : Récupération des données via Bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nomP = extras.getString("nomP");
            double prix = extras.getDouble("prix");
            String cat = extras.getString("cat");
            String imageName = extras.getString("imageName");
            ArrayList<String> genres = extras.getStringArrayList("genres");
            int idP = extras.getInt("idP");

            tvTitre.setText(nomP);

            if (imageName != null) {
                int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                if (resId != 0) ivCover.setImageResource(resId);
            }
            
            String infoTxt = "Format : " + cat + "\n" +
                            "Prix : " + prix + " €\n" +
                            "Genres : ";
            for (String g : genres) infoTxt += g + " ";
            tvInfos.setText(infoTxt);

            // Pour les détails des artistes, on repasse par le DAO (Chapitre 4)
            ProduitDAO dao = new ProduitDAO(this);
            ArrayList<Artiste> listeArtistes = dao.getArtistesByProduit(idP);

            for (Artiste a : listeArtistes) {
                TextView tvNomA = new TextView(this);
                tvNomA.setText(a.getNomA());
                tvNomA.setTextSize(18);
                tvNomA.setTextColor(Color.WHITE);
                tvNomA.setTypeface(null, android.graphics.Typeface.BOLD);
                
                TextView tvDescA = new TextView(this);
                tvDescA.setText(a.getDescription() + "\n");
                tvDescA.setTextColor(getResources().getColor(R.color.spotify_light_grey));
                tvDescA.setPadding(0, 0, 0, 16);

                llArtistes.addView(tvNomA);
                llArtistes.addView(tvDescA);
            }
        }
    }

    // Chapitre 2 : Gestion du clic sur le bouton retour (Action Bar)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ferme cette activité et revient à la précédente
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
