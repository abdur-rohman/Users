package id.refactory.users;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import id.refactory.users.adapters.UserAdapter;
import id.refactory.users.databinding.ActivityMainBinding;
import id.refactory.users.viewmodels.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private UserAdapter userAdapter = new UserAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUsers();
        userViewModel.getIsLoading().observe(this, this::showLoading);
        userViewModel.getResults().observe(this, userAdapter::setList);
        userViewModel.getError().observe(this, it -> {
            if (!TextUtils.isEmpty(it))
                Toast.makeText(MainActivity.this, it, Toast.LENGTH_LONG).show();
        });

        binding.rvUsers.setAdapter(userAdapter);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userViewModel.filterByName(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.setViewModel(userViewModel);
    }

    private void showLoading(boolean isLoading) {
        binding.pbLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.rvUsers.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;

        userViewModel.onClear();
    }
}