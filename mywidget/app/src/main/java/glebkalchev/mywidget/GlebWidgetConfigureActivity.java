package glebkalchev.mywidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * The configuration screen for the {@link GlebWidget GlebWidget} AppWidget.
 */
public class GlebWidgetConfigureActivity extends Activity {

    //Константа имени файла
    private static final String PREFS_NAME = "glebkalchev.mywidget.GlebWidget";
    //префикс данных title'a
    private static final String PREF_TITLE_PREFIX_KEY = "my_widget_title_";
    //префикс данных цвета
    private static final String PREF_COLOR_PREFIX_KEY = "my_widget_color_";

    //ID настраиваемого виджета
    //Изначально устанавливается INVALID ID, но потом должно поменяться исходя из данных в extra
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    //Текстовое поле title'a
    EditText mAppWidgetText;
    //RadioGroup
    RadioGroup mColorPicker;

    //Метод, выполняемый при нажатии на кнопку "Создать виджет"
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = GlebWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            // По нажатии на кнопку сохраняется текст из TextEdit
            String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            int colorId;
            switch (mColorPicker.getCheckedRadioButtonId()) {
                case R.id.radioButton:
                    colorId = R.color.red;
                    break;
                case R.id.radioButton2:
                    colorId = R.color.orange;
                    break;
                case R.id.radioButton3:
                    colorId = R.color.yellow;
                    break;
                case R.id.radioButton4:
                    colorId = R.color.green;
                    break;
                case R.id.radioButton5:
                    colorId = R.color.light_blue;
                    break;
                case R.id.radioButton6:
                    colorId = R.color.dark_blue;
                    break;
                case R.id.radioButton7:
                    colorId = R.color.purple;
                    break;
                default:
                    return;
            }
            saveColorPref(context, mAppWidgetId, colorId);

            Log.e("zho", "title: " + widgetText);
            Log.e("zho", "colorId: " + colorId);

            // It is the responsibility of the configuration activity to update the app widget
            //Обращаемся к widgetManager'у, чтобы тот обновил виджет
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //Обращаемся к статическому методу класса GlebWidget, чтобы обновить конкретный
            //widget по его id
            GlebWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            //Передаем в result id виджета
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    //Конструктор класса. Не трогаем. В Android-dev'e его трогать вообще не рекомендуется
    public GlebWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    //Метод для сохранения нового title'a в prefs
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        //Обращаемся к SharedPrefs и вызываем метод edit() для редактирования настроек
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        //Генерируем ключ, конкатинируя префикс title'a с id виджета, и передаем значение переменной парамера text
        prefs.putString(PREF_TITLE_PREFIX_KEY + appWidgetId, text);
        //Сохраняем изменения в prefs
        prefs.apply();
    }
    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    //Метод, возвращающий текст виджета из SharedPreferences
    static String loadTitlePref(Context context, int appWidgetId) {
        //Достаем файл настроек по константе PREFS_NAME
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //Достаем данные текста настроек
        String titleValue = prefs.getString(PREF_TITLE_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            //Нам удалось извлечь title из prefs. Возвращаем
            return titleValue;
        } else {
            //Нам не удалось достать title из prefs :(. Возвращаем дефолтное значение из ресурсов
            return context.getString(R.string.appwidget_text);
        }
    }
    //Метод для удаления данных из настроек
    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_TITLE_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    //ЦВЕТА
    // Write the prefix to the SharedPreferences object for this widget
    //Метод для сохранения нового color'a в prefs
    static void saveColorPref(Context context, int appWidgetId, int colorId) {
        //Обращаемся к SharedPrefs и вызываем метод edit() для редактирования настроек
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        //Генерируем ключ, конкатинируя префикс title'a с id виджета, и передаем значение переменной парамера text
        prefs.putInt(PREF_COLOR_PREFIX_KEY + appWidgetId, colorId);
        //Сохраняем изменения в prefs
        prefs.apply();
    }
    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    //Метод, возвращающий color виджета из SharedPreferences
    static int loadColorPref(Context context, int appWidgetId) {
        //Достаем файл настроек по константе PREFS_NAME
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //Достаем данные текста настроек
        int colorId = prefs.getInt(PREF_COLOR_PREFIX_KEY + appWidgetId, -1);
        if (colorId != -1) {
            //Нам удалось извлечь title из prefs. Возвращаем
            return colorId;
        } else {
            //Нам не удалось достать title из prefs :(. Возвращаем дефолтное значение "красный"
            return R.color.red;
        }
    }
    //Метод для удаления данных из настроек
    static void deleteColorPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_COLOR_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        //Устанавливаем layout для активити настроек
        setContentView(R.layout.gleb_widget_configure);
        //Инициализируем mAppWidgetTextEdit, чтобы потом взять из него значение
        mAppWidgetText = findViewById(R.id.appwidget_text);
        //То же самое касается и radioGroup, из которого достанем выбранный цвет
        mColorPicker = findViewById(R.id.color_picker);

        //Добавляем кнопке "добавить виджет" обзервера mOnClickListener
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        //В интенте в extra'х хранятся данные о виджете
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        //Почему-то не удалось достать id виджета из extra. Выдаем ошибку
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        //Загружаем в textEdit действующий текст из настроек
        mAppWidgetText.setText(loadTitlePref(GlebWidgetConfigureActivity.this, mAppWidgetId));
        mColorPicker.check(R.id.radioButton);
    }
}

