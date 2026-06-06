package com.example.cityguide.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cityguide.R;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private EditText etPhone, etPassword, etConfirmPassword;
    private Button btnLogin, btnRegister, btnLogout;
    private ImageView ivProfilePhoto;
    private LinearLayout userInfoLayout, inputLayout;
    private TextView tvPhone;

    private boolean isLoginMode = true;
    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private int[] avatars = {
            R.drawable.ic_avatar_cat,
            R.drawable.ic_avatar_dog,
            R.drawable.ic_avatar_bird,
            R.drawable.ic_avatar_fish,
            R.drawable.ic_avatar_rabbit
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        etPhone           = root.findViewById(R.id.etPhone);
        etPassword        = root.findViewById(R.id.etPassword);
        etConfirmPassword = root.findViewById(R.id.etConfirmPassword);
        btnLogin          = root.findViewById(R.id.btnLogin);
        btnRegister       = root.findViewById(R.id.btnRegister);
        btnLogout         = root.findViewById(R.id.btnLogout);
        ivProfilePhoto    = root.findViewById(R.id.ivProfilePhoto);
        userInfoLayout    = root.findViewById(R.id.userInfoLayout);
        tvPhone           = root.findViewById(R.id.tvPhone);
        inputLayout       = root.findViewById(R.id.inputLayout);

        btnLogin.setOnClickListener(v -> handleLoginOrRegister());
        btnRegister.setOnClickListener(v -> toggleMode());

        if (btnLogout != null)
            btnLogout.setOnClickListener(v -> logout());

        ivProfilePhoto.setOnClickListener(v -> showImagePickerDialog());

        return root;
    }

    private void showImagePickerDialog() {
        String[] options = {"Выбрать из галереи", "Сделать фото", "Выбрать аватар"};
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Выберите действие")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) openGallery();
                    else if (which == 1) openCamera();
                    else showAvatarPicker();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    private void showAvatarPicker() {
        String[] avatarNames = {"🐱 Котик", "🐶 Собака", "🐦 Птичка", "🐟 Рыбка", "🐰 Кролик"};
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Выберите аватар")
                .setItems(avatarNames, (dialog, which) -> {
                    ivProfilePhoto.setImageResource(avatars[which]);
                    ivProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Toast.makeText(getContext(), "Аватар установлен!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            getContext().getContentResolver(), imageUri);
                    ivProfilePhoto.setImageBitmap(bitmap);
                    ivProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivProfilePhoto.setImageBitmap(bitmap);
                ivProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openCamera();
            else
                Toast.makeText(getContext(), "Нужно разрешение на камеру", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLoginOrRegister() {
        String phone    = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (phone.isEmpty())    { etPhone.setError("Введите номер телефона"); etPhone.requestFocus(); return; }
        if (password.isEmpty()) { etPassword.setError("Введите пароль"); etPassword.requestFocus(); return; }

        if (isLoginMode) {
            if (validateLogin(phone, password)) {
                showProfile(phone);
                Toast.makeText(getContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Неверный номер или пароль", Toast.LENGTH_SHORT).show();
            }
        } else {
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            if (confirmPassword.isEmpty()) { etConfirmPassword.setError("Подтвердите пароль"); etConfirmPassword.requestFocus(); return; }
            if (!password.equals(confirmPassword)) { Toast.makeText(getContext(), "Пароли не совпадают!", Toast.LENGTH_SHORT).show(); return; }
            if (password.length() < 6) { Toast.makeText(getContext(), "Пароль должен быть не менее 6 символов", Toast.LENGTH_SHORT).show(); return; }

            if (registerUser(phone, password)) {
                Toast.makeText(getContext(), "Регистрация успешна! Теперь войдите", Toast.LENGTH_SHORT).show();
                etPhone.setText(""); etPassword.setText(""); etConfirmPassword.setText("");
                toggleMode();
            } else {
                Toast.makeText(getContext(), "Пользователь уже существует", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toggleMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            btnRegister.setText("ЗАРЕГИСТРИРОВАТЬСЯ");
            btnLogin.setText("ВОЙТИ");
            etConfirmPassword.setVisibility(View.GONE);
        } else {
            btnRegister.setText("УЖЕ ЕСТЬ АККАУНТ? ВОЙТИ");
            btnLogin.setText("ЗАРЕГИСТРИРОВАТЬСЯ");
            etConfirmPassword.setVisibility(View.VISIBLE);
        }
        etPhone.setText(""); etPassword.setText(""); etConfirmPassword.setText("");
    }

    private void showProfile(String phone) {
        if (inputLayout != null)    inputLayout.setVisibility(View.GONE);
        if (btnLogin != null)       btnLogin.setVisibility(View.GONE);
        if (btnRegister != null)    btnRegister.setVisibility(View.GONE);
        if (userInfoLayout != null) userInfoLayout.setVisibility(View.VISIBLE);
        if (ivProfilePhoto != null) ivProfilePhoto.setVisibility(View.VISIBLE);
        if (tvPhone != null)        tvPhone.setText("Номер: " + phone);
        if (btnLogout != null)      btnLogout.setVisibility(View.VISIBLE);
    }

    private void logout() {
        isLoginMode = true;
        if (inputLayout != null)    inputLayout.setVisibility(View.VISIBLE);
        if (userInfoLayout != null) userInfoLayout.setVisibility(View.GONE);
        if (btnLogin != null)       btnLogin.setVisibility(View.VISIBLE);
        if (btnRegister != null)    btnRegister.setVisibility(View.VISIBLE);
        if (btnLogout != null)      btnLogout.setVisibility(View.GONE);
        if (etPhone != null)        etPhone.setText("");
        if (etPassword != null)     etPassword.setText("");
        if (etConfirmPassword != null) etConfirmPassword.setText("");
        Toast.makeText(getContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
    }

    private boolean validateLogin(String phone, String password) {
        return phone.length() >= 10 && password.length() >= 6;
    }

    private boolean registerUser(String phone, String password) {
        return true;
    }
}
