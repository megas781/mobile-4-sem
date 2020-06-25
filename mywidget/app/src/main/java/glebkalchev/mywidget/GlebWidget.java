package glebkalchev.mywidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.logging.Logger;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link GlebWidgetConfigureActivity GlebWidgetConfigureActivity}
 */
public class GlebWidget extends AppWidgetProvider {

    //Кастомный метод для обновления данных виджета по id
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //Достаем значения из prefs, чтобы установить их в view виджета
        CharSequence widgetText = GlebWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        int colorId = GlebWidgetConfigureActivity.loadColorPref(context, appWidgetId);

        Log.e("piz", "widgetText: " + widgetText);
        Log.e("piz", "colorId: " + colorId);

        // Construct the RemoteViews object
        // Inflate'им layout для виджета по средствам класса RemoteViews
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.gleb_widget);
        view.setTextViewText(R.id.widget_text_view, widgetText);
        view.setInt(R.id.widget_text_view, "setBackgroundColor", context.getResources().getColor(colorId));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, view);
    }

    //Метод жизненного цикла виджетов, выполняемый переодически для всех сразу
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            //При удалении виджета удалем данные о заголовке и цвете
            GlebWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
            GlebWidgetConfigureActivity.deleteColorPref(context, appWidgetId);
        }
    }

    //Метода, выполняемый, когда виджет создается
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    //Метод, выполняемый, когда последний созданный виджет удаляется
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

