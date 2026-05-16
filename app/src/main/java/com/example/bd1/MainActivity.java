package com.example.bd1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner spCategorie, spGenre, spSort;
    private EditText etSearch;
    private Button btnRechercher;
    private LinearLayout llResults;
    private Produit selectedProduit;
    private ProduitDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spCategorie = findViewById(R.id.spinner_categorie);
        spGenre = findViewById(R.id.spinner_genre);
        spSort = findViewById(R.id.spinner_sort);
        etSearch = findViewById(R.id.et_search);
        btnRechercher = findViewById(R.id.btn_rechercher);
        llResults = findViewById(R.id.ll_results);

        dao = new ProduitDAO(this);

        setupSpinners();

        btnRechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechercher();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rechercher();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        rechercher();
    }

    private void setupSpinners() {
        ArrayList<Categorie> categories = dao.getCategories();
        ArrayAdapter<Categorie> adapterC = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategorie.setAdapter(adapterC);

        ArrayList<Genre> genres = dao.getGenres();
        ArrayAdapter<Genre> adapterG = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genres);
        adapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenre.setAdapter(adapterG);

        ArrayList<String> sortOptions = new ArrayList<>();
        sortOptions.add(getString(R.string.price_croissant));
        sortOptions.add(getString(R.string.price_decroissant));
        ArrayAdapter<String> adapterS = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortOptions);
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(adapterS);
    }

    private void rechercher() {
        Categorie selectedCat = (Categorie) spCategorie.getSelectedItem();
        Genre selectedGenre = (Genre) spGenre.getSelectedItem();
        String sortOption = (String) spSort.getSelectedItem();
        String order = sortOption != null && sortOption.equals(getString(R.string.price_croissant)) ? "ASC" : "DESC";
        String searchTerm = etSearch.getText().toString();

        ArrayList<Produit> produits = dao.getProduits(selectedCat != null ? selectedCat.getIdC() : 0, 
                                                     selectedGenre != null ? selectedGenre.getIdG() : 0, 
                                                     order, searchTerm);

        llResults.removeAllViews();

        for (final Produit p : produits) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setPadding(0, 32, 0, 32);
            itemLayout.setClickable(true);
            itemLayout.setFocusable(true);
            itemLayout.setBackgroundResource(android.R.color.transparent);
            itemLayout.setTag(p);
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

            /* Ici c'est pour l'affichage de l'image */
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
            tvSub.setText(p.getNomCategorie());
            tvSub.setTextColor(getResources().getColor(R.color.spotify_light_grey));
            tvSub.setTextSize(14);

            TextView tvDetails = new TextView(this);
            String artists = "";
            for (String a : p.getArtistes()) artists += a + ", ";
            if (artists.endsWith(", ")) artists = artists.substring(0, artists.length() - 2);
            
            tvDetails.setText(artists + " • " + p.getPrix() + " €");
            tvDetails.setTextColor(getResources().getColor(R.color.spotify_light_grey));
            tvDetails.setTextSize(14);

            textLayout.addView(tvTitle);
            textLayout.addView(tvSub);
            textLayout.addView(tvDetails);

            contentLayout.addView(ivCover);
            contentLayout.addView(textLayout);

            itemLayout.addView(contentLayout);
            llResults.addView(itemLayout);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_view_favoris) {
            startActivity(new Intent(this, FavorisActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        selectedProduit = (Produit) v.getTag();
        getMenuInflater().inflate(R.menu.product_context_menu, menu);
        
        MenuItem favoriteItem = menu.findItem(R.id.action_toggle_favori);
        if (selectedProduit.isFavori()) {
            favoriteItem.setTitle(R.string.menu_remove_favori);
        } else {
            favoriteItem.setTitle(R.string.menu_add_favori);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedProduit == null) return super.onContextItemSelected(item);

        if (item.getItemId() == R.id.action_detail) {
            ouvrirDetail(selectedProduit);
            return true;
        } else if (item.getItemId() == R.id.action_toggle_favori) {
            dao.updateFavoris(selectedProduit.getIdP(), !selectedProduit.isFavori());
            rechercher();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
