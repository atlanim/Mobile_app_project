package ca.ulaval.ima.mp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ConnectionFragment.OnFragmentConnectionInteractionListener, SignInFragment.OnFragmentInteractionListener {


    private final String message = "spotify login with success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment newFragment = new ConnectionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Main_container, newFragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void displayToast() {

            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });

    }

    @Override
    public void openHomeFragment(UserParameters userParameters) {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("currentUserParameters", userParameters);
        startActivity(intent);
    }

    @Override
    public void showAlertDiaglog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The combinaison of username and password is incorrect");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
