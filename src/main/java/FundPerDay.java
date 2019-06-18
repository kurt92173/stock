import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Comparator;

public class FundPerDay {
    private static final int SIZE = 10;
    private static final int INDEX_NONE = 0;
    private static final int INDEX_ID = 1;
    private static final int INDEX_NAME = 2;
    private static final int IDX_BUYUNIT = 3;
    private static final int IDX_SELLUNIT = 4;
    private static final int IDX_DIFFUNIT = 5;

    private static final String DATE = "2019"+"0617";
    private static final String PATH = "D:\\fund\\"+ DATE+".csv";
    private static final String URL = "http://www.twse.com.tw/fund/TWT44U?response=json&date="+DATE;

    public static void getCsv() {
        try {
            URL website = new URL(URL);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());

            FileOutputStream fos = new FileOutputStream(PATH);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void show() {
        try {
            File csv = new File(PATH);
            if (!csv.exists()) throw new Exception("Csv is not existed.");

            BufferedReader reader = new BufferedReader(new FileReader(csv));
            String currentLine;
            ArrayList<FundPerDayView> views = null;
            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();

                views = parseResult(trimmedLine);
                views.sort(Comparator.comparing(FundPerDayView::getDiffUnit).reversed());
            }
            reader.close();
            for (FundPerDayView view:views) {
                System.out.println(new Gson().toJson(view));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ArrayList<FundPerDayView> parseResult(String result) throws Exception {
        ArrayList<FundPerDayView> views = new ArrayList<FundPerDayView>();
        try {
            JsonReader reader = new JsonReader(new StringReader(result));
            reader.setLenient(true);

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (!"data".equals(name)) {
                    reader.skipValue();
                    continue;
                }

                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginArray();
                    int index = 0;
                    FundPerDayView view = new FundPerDayView();
                    while (reader.hasNext()) {
                        String temp = reader.nextString().trim().replace(",", "");
                        switch (index) {
                            case INDEX_NONE:
                                break;
                            case INDEX_ID:
                                view.setId(temp);
                                break;
                            case INDEX_NAME:
                                view.setName(temp);
                                break;
                            case IDX_BUYUNIT:
                                view.setBuyUnit(Integer.valueOf(temp));
                                break;
                            case IDX_SELLUNIT:
                                view.setSellUnit(Integer.valueOf(temp));
                                break;
                            case IDX_DIFFUNIT:
                                view.setDiffUnit(Integer.valueOf(temp));
                                break;
                        }
                        index++;
                    }

                    views.add(view);
                    reader.endArray();
                }
                reader.endArray();
            }

            return views;
        } catch (Exception e) {
            throw e;
        }
    }



    public static void main(String[] args) throws Exception {
        getCsv();
        show();
    }
}
