package com.example.bd1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class FavorisActivity extends AppCompatActivity {

    private LinearLayout llResults;
    private ProduitDAO dao;
    private Produit selectedProduit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_favoris);
        }

        llResults = findViewById(R.id.ll_favoris_results);
        dao = new ProduitDAO(this);

        chargerFavoris();
    }

    private void chargerFavoris() {
        llResults.removeAllViews();
        ArrayList<Produit> favoris = dao.getFavoris();

        for (final Produit p : favoris) {
            LinearLayout itemLayout = creerItemLayout(p);
            llResults.addView(itemLayout);
        }
    }

    private LinearLayout creerItemLayout(final Produit p) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setPadding(0, 32, 0, 32);
        itemLayout.setClickable(true);
        itemLayout.setFocusable(true);
        itemLayout.setTag(p); // Store product in tag for context menu

        registerForContextMenu(itemLayout);

        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirDetail(p);
            }
        });

        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView ivCover = new ImageView(this);
        int resId = getResources().getIdentifier(p.getImageName(), "drawable", getPackageName());
        ivCover.setImageResource(resId != 0 ? resId : android.R.drawable.ic_menu_gallery);
        
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(240, 240);
        ivParams.setMargins(0, 0, 32, 0);
        ivCover.setLayoutParams(ivParams);
        ivCover.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(p.getNomP());
        tvTitle.setTextSize(18);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvSub = new TextView(this);
        tvSub.setText(p.getNomCategorie() + " • " + p.getPrix() + " €");
        tvSub.setTextColor(getResources().getColor(R.color.spotify_light_grey));
        tvSub.setTextSize(14);

        textLayout.addView(tvTitle);
        textLayout.addView(tvSub);

        contentLayout.addView(ivCover);
        contentLayout.addView(textLayout);
        itemLayout.addView(contentLayout);

        return itemLayout;
    }

    private void ouvrirDetail(Produit p) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("idP", p.getIdP());
        bundle.putString("nomP", p.getNomP());
        bundle.putDouble("prix", p.getPrix());
        bundle.putString("cat", p.getNomCategorie());
        bundle.putString("imageName", p.getImageName());
        bundle.putStringArrayList("genres", p.getGenres());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        selectedProduit = (Produit) v.getTag();
        getMenuInflater().inflate(R.menu.product_context_menu, menu);
        menu.findItem(R.id.action_toggle_favori).setTitle(R.string.menu_remove_favori);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedProduit == null) return super.onContextItemSelected(item);

        if (item.getItemId() == R.id.action_detail) {
            ouvrirDetail(selectedProduit);
            return true;
        } else if (item.getItemId() == R.id.action_toggle_favori) {
            dao.updateFavoris(selectedProduit.getIdP(), false);
            chargerFavoris();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
