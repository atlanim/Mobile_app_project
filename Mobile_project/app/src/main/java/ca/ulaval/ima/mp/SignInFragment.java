package ca.ulaval.ima.mp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SignInFragment extends Fragment {
    private String TAG = "Sign_in";

    @Override
    public void onStart() {
        super.onStart();
    }

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button Sign_in = view.findViewById(R.id.SignIn_Button);

        final Button createAccount = view.findViewById(R.id.CreateAccount_button);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sign_up(view);
            }
        });

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText Email_edit = view.findViewById(R.id.Email);
                EditText Password_edit =  view.findViewById(R.id.Password);

                String email = Email_edit.getText().toString();
                String password = Password_edit.getText().toString();

            }
        });

    }



    public void Sign_up(View view)
    {
       /* SignUpFragment signUp = new SignUpFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.Main_container, signUp);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d(TAG, "wtf");*/

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
