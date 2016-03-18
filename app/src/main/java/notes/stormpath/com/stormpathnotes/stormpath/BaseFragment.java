package notes.stormpath.com.stormpathnotes.stormpath;


import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class BaseFragment extends Fragment {

    protected ImageView logo;

    protected EditText usernameInput;

    protected EditText passwordInput;

    protected ProgressBar progressBar;

    protected Button loginButton;

    protected Button registerButton;

    protected Button resetPasswordButton;

    protected EditText firstNameEditText;

    protected EditText surnameEditText;

    protected EditText emailEditText;

    protected EditText passwordEditText;

    protected Button sendButton;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();


    }


    protected boolean isResourceIdInPackage(String packageName, int resId){
        if(packageName == null || resId == 0){
            return false;
        }

        Resources res = null;
        if(packageName.equals(getActivity().getPackageName())){
            res = getActivity().getResources();
        }else{
            try{
                res = getActivity().getPackageManager().getResourcesForApplication(packageName);
            }catch(PackageManager.NameNotFoundException e){
                //Log.w("", packageName + "does not contain " + resId + " ... " + e.getMessage());
            }
        }

        if(res == null){
            return false;
        }

        return isResourceIdInResources(res, resId);
    }

    private boolean isResourceIdInResources(Resources res, int resId){

        try{
            getActivity().getResources().getResourceName(resId);

            //Didn't catch so id is in res
            return true;

        }catch (Resources.NotFoundException e){
            return false;
        }
    }



}
