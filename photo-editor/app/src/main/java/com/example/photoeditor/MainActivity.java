package com.example.photoeditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //Код перехода к камере
    private static final int CAMERA_REQUEST = 0;
    //Код перехода к запросу на разрешение использования камеры
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0;
    //
    private static final int PICTURE_CROP = 1;
    //Ссылка imageview
    private ImageView imageView;
    //uri-ссылка на файл фотографии
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        //Проверка API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Спрашиваем разрешение
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }
    }
    public void onClick(View view) {
        getFullPhoto();
    }

    //Метод выполняемый по возвращении от другой активности
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Проверка результата
        if(resultCode == RESULT_OK) {
            //Если всё "ОК", то смотрим, откуда мы пришли
            if(requestCode == CAMERA_REQUEST) {
                //Мы пришли от приложения камеры. В таком случае устанавливаем изображение
                imageView.setImageURI(photoUri);
                //Теперь нужно его обрезать. Вызываем активность для обрезки фото
                crop();
            }
            //Если вернулись от crop-activity, то
            if (requestCode == PICTURE_CROP) {
                //Достаем данные из прикрепленного intent'a data
                Bundle extras = data.getExtras();
                //Достаем изображение с помощью getParcelable
                Bitmap picture = extras.getParcelable("data");
                //Устанавливаем новое значение картинке
                imageView.setImageBitmap(picture);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //В хорошем случае ничего не делаем
                } else {
                    //Если разрешение не дано, то выводим сообщение об ошибке
                    Toast.makeText(this, "Невозможно кадрировать", Toast.LENGTH_LONG).show();
                }
        }
    }

    //Метод создания intent'a для камеры
    private void getFullPhoto() {
        Intent cameraIntent = new Intent();
        //Устанавливаем интенту действие захвата изображения (т.е. снимка)
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //Если есть активность, которая может обработать этот intent, то продолжаем
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File file = null;
            try {
                //Пытаемся создать файл, в который позже будем записывать изображение
                file = createPhotoFile();
            } catch (IOException error) {
                //Если не получилось, отображаем ошибку
                Toast.makeText(this, "Ошибка при создании файла", Toast.LENGTH_LONG).show();
            }
            //Если файл был создан
            if (file != null){
                //Проверяем версию API, чтобы
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //Нужно достать uri файла, который был только что инициализирован
                    photoUri = FileProvider.getUriForFile(
                            this, "com.example.android.provider", file);
                } else {
                    photoUri = Uri.fromFile(file);
                }
                //Обязательно указываем путь, куда записывать изображение
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //Запускаем активность
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        }
    }

    //Метод создания файла
    private File createPhotoFile() throws IOException {
        //Создаем строковое human-friendly значение timestamp'a
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //Создаем файл в стандартной директории изображений Android'a
        File file = File.createTempFile(timeStamp, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        return  file;
    }
    //Метод переводящий на активность обрезки
    private void crop() {
        try {
            //Создается интент для активити с crop'ом
            Intent cropIntent = new Intent();
            //Устанавливается действие CROP
            cropIntent.setAction("com.android.camera.action.CROP");

            //Дополнительно спрашиваем разрешения в интенте на чтение и запись URI фотографии
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            //Устанавливаем uri файла, который нужно обрезать и его тип
            cropIntent.setDataAndType(photoUri,"image/*");
            cropIntent.putExtra("crop", true);
            //растягивать изображение не нужно. Поэтому aspectX и aspectY равны единице
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //Выходной размер - 256 px
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //Выходной формат изображения – JPEG
            cropIntent.putExtra("outputFormat", "JPEG" );
            //Данные нужно не только перезаписать, но и вернуть
            //сюда в активность, чтобы позже отобразить
            cropIntent.putExtra("return-data", true);
            //Показываем crop-активность
            startActivityForResult(cropIntent, PICTURE_CROP);
        } catch (ActivityNotFoundException error) {
            //Если активность не найдена, то отображаем сообщение о невозможности обрезки фотографии
            Toast.makeText(this, "Невозможно кадрировать", Toast.LENGTH_LONG).show();
        }
    }
}
