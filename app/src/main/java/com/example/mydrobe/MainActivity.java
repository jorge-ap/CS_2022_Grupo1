package com.example.mydrobe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1;
    private static final int REGISTER_POINTS = 10000000;
    private static final int SELECT_FILE = 10;
    private Random random = new Random();
    private int modo = 0;
    private List<String> poolNormalSentences;
    private List<String> poolObsceneSentences;
    private final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "usuario.bat");
    private Usuario usuario = new Usuario();

    private TextView txPuntos;
    private MediaPlayer mpNormal;
    private MediaPlayer mpObscene;
    private Drawable skin = null;

    private int skinActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeSystem();
        setContentView(R.layout.activity_main);
        txPuntos = findViewById(R.id.tx_puntos);
        txPuntos.setText(String.valueOf(usuario.getContador()));
        frasesPredeterminadas();
        mpNormal = MediaPlayer.create(this, R.raw.audiobtnnormal);
        mpObscene = MediaPlayer.create(this, R.raw.audiobtnobsceno);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveUser();
    }

    private boolean checkPermissions() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Deseas salis de MYDrove?")
                    .setPositiveButton("Si", (dialogInterface, i) -> {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        saveUser();
                    })
                    .setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void saveUser() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(usuario);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeSystem() { //Cargamos el ususario y las frases en el MainActivity..
        //Cargamos el usuario
        long files = file.length();
        boolean x = files == 0;
        if (!x) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                this.usuario = (Usuario) ois.readObject();
                ois.close();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

        }
        if (!file.exists() && checkPermissions()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Method to click
     *
     * @param view The view of the instance
     */
    public void clicker(View view) {
        usuario.clicar();
        txPuntos.setText(Integer.toString(usuario.getContador()));
        if (modo == 0) {
            randomSentence(usuario.getNormalSentencePool());
            mpNormal.start();
        } else {
            randomSentence(usuario.getObsceneSentencePool());
            mpObscene.start();
        }
    }

    public void randomSentence(@NonNull List<String> sentencesPool) {
        int randomRange = sentencesPool.size();
        int randomValue = random.nextInt(randomRange);
        String sentence = sentencesPool.get(randomValue);
        TextView randomSentence = findViewById(R.id.tx_frases_bonitas);
        randomSentence.setText(sentence);
    }


    public void setpoolNormalSentences(ArrayList<String> poolNormalSentences) {
        this.poolNormalSentences = poolNormalSentences;
    }

    public void setpoolObsceneSentences(ArrayList<String> poolObsceneSentences) {
        this.poolObsceneSentences = poolObsceneSentences;
    }

    /**
     * Cambia la interfaz a la tienda
     * @param view The view of the instance
     */
    public void showTienda(@SuppressWarnings("UnusedParameters") View view) {
        setContentView(R.layout.interfaztienda);
        txPuntos = (TextView) findViewById(R.id.tx_puntos_tienda);
        txPuntos.setText(Integer.toString(usuario.getContador()));
    }

    /**
     * Cambia la interfaz a la tienda de skins
     *
     * @param view The view of the instance
     */
    public void showSkinsStore(@SuppressWarnings("UnusedParameters") View view) {
        setContentView(R.layout.interfaztiendaskins);
    }

    /**
     * Cambia la interfaz al menu obsceno
     *
     * @param view The view of the instance
     */
    public void showObsceno(@SuppressWarnings("UnusedParameters") View view) {
        modo = 1;
        setContentView(R.layout.interfazobscene);
        txPuntos = (TextView) findViewById(R.id.tx_puntos);
        txPuntos.setText(Integer.toString(usuario.getContador()));
    }

    /**
     * Cambia la interfaz al menu normal
     *
     * @param view The view of the instance
     */
    public void showMenu(@SuppressWarnings("UnusedParameters") View view) {
        modo = 0;
        setContentView(R.layout.activity_main);
        txPuntos = (TextView) findViewById(R.id.tx_puntos);
        txPuntos.setText(Integer.toString(usuario.getContador()));
        setSkin(view);
    }

    /**
     * Cambia la interfaz a el formulario para crear frases propias
     *
     * @param view The view of the instance
     */
    public void showCrearFrase(@SuppressWarnings("UnusedParameters") View view) {
        setContentView(R.layout.frases_custom);
    }

    /**
     * Vuelve a la interfaz anterior a tienda
     *
     * @param view The view of the instance
     */
    public void atras(View view) {
        if (modo == 0) {
            showMenu(view);

        } else
            showObsceno(view);
    }

    /**
     * Vuelve a la interfaz anterior a tienda skins
     *
     * @param view The view of the instance
     */
    public void atras2(View view) {
        showTienda(view);

    }

    /**
     * Establece la skin que el usuario tenga selecionada
     *
     * @param view The view of the instance
     */
    public void setSkin(View view) {
        Button buttonMain = findViewById(R.id.bt_moneda);

        switch (skinActual) {
            case 0:
                buttonMain.setForeground(getDrawable(R.drawable.efecto_btn_moneda));
                break;
            case 1:
                buttonMain.setForeground(getDrawable(R.drawable.skin_castana));
                usuario.getSkinsCompradas().add("Castaña");
                break;
            case 2:
                buttonMain.setForeground(getDrawable(R.drawable.skin_pikachu));
                usuario.getSkinsCompradas().add("Pikachu");
                break;
            case 3:

                buttonMain.setForeground(getDrawable(R.drawable.skin_steve));
                usuario.getSkinsCompradas().add("Steve");
                break;
            case 4:
                buttonMain.setForeground(getDrawable(R.drawable.skin_shrek));
                usuario.getSkinsCompradas().add("Shrek");
                break;
            case 5:
                buttonMain.setForeground(skin);
                break;
            default:
                System.err.println("Something has gone wrong");
                break;
        }
    }

    /**
     * Permite al usuario comprar skins si tiene los puntos necesarios
     *
     * @param view The view of the instance
     */
    public void pressed(View view) {
        switch (view.getId()) {
            case R.id.btn_defecto:
                skinActual = 0;
                break;
            case R.id.btn_skin_1:
                if (usuario.getSkinsCompradas().contains("Castaña") || usuario.pago(500)) {
                    skinActual = 1;
                }
                break;
            case R.id.btn_skin_2:
                if (usuario.getSkinsCompradas().contains("Pikachu") || usuario.pago(500)) {
                    skinActual = 2;
                }
                break;
            case R.id.btn_skin_3:
                if (usuario.getSkinsCompradas().contains("Steve") || usuario.pago(500)) {
                    skinActual = 3;
                }
                break;
            case R.id.btn_skin_4:
                if (usuario.getSkinsCompradas().contains("Shrek") || usuario.pago(500)) {
                    skinActual = 4;
                }
                break;
            case R.id.btn_galeria:
                skinActual = 5;
                loadImage();
                if (skin == null) {
                    skinActual = 0;
                }
                break;
            default:
                System.err.println("Something has gone wrong");
                break;
        }
    }

    //Permite cargar una image para ser utilizada como skin
    private void loadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent, "Seleccione la galería"), SELECT_FILE);
    }

    //Transforma la image al tipo de file compatible con skin
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Uri path = Objects.requireNonNull(data).getData();
                InputStream image = getContentResolver().openInputStream(path);
                Bitmap view = BitmapFactory.decodeStream(image);
                skin = new BitmapDrawable(getResources(), view);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            skin = null;
        }
    }

    /**
     * Permite al usuario auimentar el numero de puntos obtenidos al hacer click a cambio de una cantidad de puntos
     *
     * @param view The viwe of the instance
     */
    public void mejorarClicks(View view) {
        if (usuario.pago(usuario.getValorClick() * 10)) {
            usuario.aplicarMejoraClicks();
            txPuntos.setText(String.valueOf(usuario.getContador()));
        } else {
            Snackbar mySnackbar = Snackbar.make(view, "No tienes dinero suficiente", 1000);
            mySnackbar.show();
        }
    }

    /**
     * Permite al usuario crear una frase propia para ser añadida a su pool de frases a cambio de una cantidad de puntos
     *
     * @param view The view of the instance
     */
    public void crearFrase(View view) {
        EditText eText = (EditText) findViewById(R.id.frasesCreadas);
        String str = eText.getText().toString();
        if (usuario.pago(50)) {
            if (modo == 0) {
                usuario.anadirFrase(usuario.getNormalSentencePool(), str);
            } else {
                usuario.anadirFrase(usuario.getObsceneSentencePool(), str);
            }
            showTienda(view);
        } else {
            Snackbar mySnackbar = Snackbar.make(view, "No tienes dinero suficiente", 1000);
            mySnackbar.show();
        }
    }

    /**
     * Permite al usuario agregar una frase aleatoria a su pool de frases a cambio de una cantidad de puntos
     *
     * @param view The view of the instance
     */
    public void comprarFrase(View view) {
        String frase;
        if (usuario.pago(25)) {
            if (modo == 0) {
                frase = usuario.yaEstaFrase(poolNormalSentences, usuario.getNormalSentencePool());
                usuario.anadirFrase(usuario.getNormalSentencePool(), frase);
            } else {
                frase = usuario.yaEstaFrase(poolObsceneSentences, usuario.getObsceneSentencePool());
                usuario.anadirFrase(usuario.getObsceneSentencePool(), frase);
            }
            if (frase == null) {
                Snackbar mySnackbar = Snackbar.make(view, "Ya has desbloqueado todas las frases", 1000);
                mySnackbar.show();
                usuario.setContador(usuario.getContador() + 30);
            }
        }
    }

    //Establece las frases iniciales.
    public void frasesPredeterminadas() {
        String a = "";
        ArrayList<String> n = usuario.getNormalSentencePool();
        n.add(a);
        ArrayList<String> o = usuario.getObsceneSentencePool();
        o.add(a);
        ArrayList<String> normales = new ArrayList<>(
                Arrays.asList("El único modo de hacer un gran trabajo es amar lo que haces", "Cuanto más duramente trabajo, más suerte tengo",
                        "La lógica te llevará de la a a la z. la imaginación te llevará a cualquier lugar", "A veces la adversidad es lo que necesitas encarar para ser exitoso"));
        setpoolNormalSentences(normales);
        ArrayList<String> obscenas = new ArrayList<>(
                Arrays.asList("El metodo cascada es el mejor", "ETA es una gran nación", "Lo que nosotros hemos hecho, cosa que no hizo usted, es engañar a la gente",
                        "Tú y yo tenemos una cita y tu ropa no está invitada.", "Tienes cara de ser el 9 que le falta a mi 6."));
        setpoolObsceneSentences(obscenas);
    }

    /**
     * Permite al usuario reiniciar su progresso a cambio de obtener más puntos al hacer click permanentemente
     *
     * @param view The view of the instance
     */
    public void modoPrestigio(View view) {
        if (usuario.getContador() > REGISTER_POINTS) {
            usuario.setModoPrestigio();
        } else {
            Toast toast = Toast.makeText(this, "Consigue " + REGISTER_POINTS + " puntos para desbloquear esta opción", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Muestra al ususario una ayuda textual unica en cada interfaz
     *
     * @param view The view of the instance
     */
    public void ayuda(View view) {
        TextView ab = findViewById(R.id.ayudaBoton);
        TextView af = findViewById(R.id.ayudaFrases);
        TextView ap = findViewById(R.id.ayudaPuntuacion);
        TextView at = findViewById(R.id.ayudaTienda);
        TextView ao = findViewById(R.id.modoObsceno);
        if (ab.getVisibility() == View.VISIBLE) { //si es Visible lo pones Gone
            ab.setVisibility(View.GONE);
            af.setVisibility(View.GONE);
            ap.setVisibility(View.GONE);
            at.setVisibility(View.GONE);
            ao.setVisibility(View.GONE);
        } else { // si no es Visible, lo pones
            ab.setVisibility(View.VISIBLE);
            af.setVisibility(View.VISIBLE);
            ap.setVisibility(View.VISIBLE);
            at.setVisibility(View.VISIBLE);
            ao.setVisibility(View.VISIBLE);
        }
    }


    public void ACF(View view) {
        TextView ae = findViewById(R.id.ayudaEsc);
        TextView ac = findViewById(R.id.ayudaCrear);
        if (ae.getVisibility() == View.VISIBLE) { //si es Visible lo pones Gone
            ae.setVisibility(View.GONE);
            ac.setVisibility(View.GONE);
        } else { // si no es Visible, lo pones
            ae.setVisibility(View.VISIBLE);
            ac.setVisibility(View.VISIBLE);
        }
    }

    public void ACTS(View view) {
        TextView ae = findViewById(R.id.defecto);
        TextView ac = findViewById(R.id.elegir);
        if (ae.getVisibility() == View.VISIBLE) { //si es Visible lo pones Gone
            ae.setVisibility(View.GONE);
            ac.setVisibility(View.GONE);
        } else { // si no es Visible, lo pones
            ae.setVisibility(View.VISIBLE);
            ac.setVisibility(View.VISIBLE);
        }
    }

    public void ACT(View view) {
        TextView ae = findViewById(R.id.generador);
        TextView ac = findViewById(R.id.creador);
        TextView af = findViewById(R.id.skins);
        TextView ad = findViewById(R.id.mb);
        TextView ag = findViewById(R.id.ayudaPrestigio);
        if (ae.getVisibility() == View.VISIBLE) { //si es Visible lo pones Gone
            ae.setVisibility(View.GONE);
            ac.setVisibility(View.GONE);
            af.setVisibility(View.GONE);
            ad.setVisibility(View.GONE);
            ag.setVisibility(View.GONE);
        } else { // si no es Visible, lo pones
            ae.setVisibility(View.VISIBLE);
            ac.setVisibility(View.VISIBLE);
            af.setVisibility(View.VISIBLE);
            ad.setVisibility(View.VISIBLE);
            ag.setVisibility(View.VISIBLE);
        }
    }
}