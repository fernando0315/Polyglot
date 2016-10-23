package com.example.reyhan.polyglot;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates fetching the languages and displaying it as a {@link ListView} layout.
 */
public class LanguageListFragment extends Fragment {

    private LanguageAdapter mAdapter;
    private String [] web;
    private Map<String, Language> languages;
    private Language templanguage;
    private View rootView;

    public LanguageListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*****
         * put every language source code for the api and picture id in the HashMap
         */
        languages = new HashMap<String, Language>();

        templanguage = new Language("en-US, ZiraRUS", R.drawable.us_flag);
        languages.put("English (USA)", templanguage);
        templanguage = new Language("en-GB, Susan, Apollo", R.drawable.br_flag);
        languages.put("English (UK)", templanguage);
        templanguage = new Language("fr-CA, Caroline", R.drawable.fr_flag);
        languages.put("French", templanguage);
        templanguage = new Language("de-DE, Hedda", R.drawable.de_flag);
        languages.put("German", templanguage);
        templanguage = new Language("zh-CN, HuihuiRUS", R.drawable.zh_flag);
        languages.put("Chinese", templanguage);
        templanguage = new Language("it-IT, Cosimo, Apollo", R.drawable.it_flag);
        languages.put("Italian", templanguage);
        templanguage = new Language("ja-JP, Ayumi, Apollo", R.drawable.ja_flag);
        languages.put("Japanese", templanguage);
        templanguage = new Language("pt-BR, Daniel, Apollo", R.drawable.ru_flag);
        languages.put("Russia", templanguage);
        templanguage = new Language("es-ES, Pablo, Apollo", R.drawable.es_flag);
        languages.put("Spanish", templanguage);




        rootView = inflater.inflate(R.layout.fragment_languagelist, container, false);

        // Create an adapter to bind the items with the view
        mAdapter = new LanguageAdapter(getActivity(), languages);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_language);
        listView.setAdapter(mAdapter);

        return rootView;
    }

    /********
     * Edited ArrayAdapter which will set picture and button for the listview elements
     */
    class LanguageAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] web;
        private final Map<String, Language> languages;

        public LanguageAdapter(Activity context, Map<String, Language> languages) {
            super(context, R.layout.list_item_language, languages.keySet().toArray(new String[languages.size()]));
            this.context = context;
            this.languages = languages;
            web = languages.keySet().toArray(new String[languages.size()]);

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_item_language, null, true);
            Button country = (Button) rowView.findViewById(R.id.list_item_language_textview);

            country.setText(web[position]);

            country.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    updateLanguage(web[position]);
                }
            });

            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

            imageView.setImageResource(languages.get(web[position]).getimage());

            return rowView;
        }
    }

    /********
     * Update the UI in the other fragment and go to the language chosen fragment
     * open a server for the raspberry pi to get the source code from here
     * @param language
     */
    public void updateLanguage(String language)
    {
        Thread socketServerThread = new Thread(new SocketServerThread(languages.get(language).getsourcecode()));
        socketServerThread.start();
        Fragment newfragment = new LanguageChosenFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("lang", languages.get(language).getimage());
        newfragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, newfragment);
        fragmentTransaction.commit();
    }

    /**********
     * server io to send data
     */
    public class SocketServerThread extends Thread {

        static final int SocketServerPORT = 5007;
        String message;
        ServerSocket serverSocket;
        Socket socket;

        public SocketServerThread(String language)
        {
            message = language;

            if(socket != null && !socket.isClosed())
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if(serverSocket != null && !serverSocket.isClosed())
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        @Override
        public void run() {
            try {
                Log.d("socket", "socket open");
                serverSocket = new ServerSocket(SocketServerPORT);
                socket = serverSocket.accept();
                Log.d("socket", "before output after socket");
                OutputStream outputStream;
                outputStream = socket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(message);
                printStream.close();
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}