package com.example.petdispenser;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.petdispenser.dao.AnimaleDAO;
import com.example.petdispenser.dao.DietaDAO;
import com.example.petdispenser.dao.PastoDAO;
import com.example.petdispenser.models.Animal;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.Pasto;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class calendar extends Fragment {

    CalendarView calendar;


    String[] eventi = {};
    ArrayList<String> queryEventi = new ArrayList<>();
    ArrayAdapter adapter;
    ListView listaEventi;
    AnimaleDAO animaleDAO;
    DietaDAO dietaDAO;
    PastoDAO pastoDAO;
    private MainActivityToolbarListener itemDetailFragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.calendar_fragment, container, false);

        this.animaleDAO = new AnimaleDAO(v.getContext());
        this.dietaDAO = new DietaDAO(v.getContext());
        this.pastoDAO = new PastoDAO(v.getContext());


        calendar =  v.findViewById(R.id.calendarViewF);
        calendar.showCurrentMonthPage();

       listaEventi = (ListView) v.findViewById(R.id.listaEventiCalendar);
        //adapter = new ArrayAdapter<String>(v.getContext(),R.layout.activity_listview,eventi);
      //  listaEventi.setAdapter(adapter);



        List<Pasto> pastoListOfTheDay = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String ggMMAAAA = formatter.format(date).toString();
        ggMMAAAA = deleteUselessZero(ggMMAAAA);
        try {
            pastoListOfTheDay = pastoDAO.getPastiOfDate(ggMMAAAA);// carico i pasti della giornata
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        queryEventi.clear();

        if (pastoListOfTheDay != null && ! pastoListOfTheDay.isEmpty()){
            for (Pasto p : pastoListOfTheDay)
            {
                Diet dieta = new Diet();
                try {
                    if(dietaDAO.getDietById(p.getDietaId())!= null)
                        dieta = dietaDAO.getDietById(p.getDietaId());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                try {
                    if( animaleDAO.getAnimalById(dieta.getAnimalId())!= null) {
                        String nomeAnimale = animaleDAO.getAnimalById(dieta.getAnimalId()).getName();
                        queryEventi.add(nomeAnimale+" -> "+p.getName() + ": " + p.getQcroccantini()
                                + "g croccantini " + p.getQumido() + "g umido");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        eventi = new String[queryEventi.size()];
        eventi = queryEventi.toArray(eventi);
        adapter = new ArrayAdapter<>(v.getContext(),R.layout.activity_listview,eventi);
        listaEventi.setAdapter(adapter);



        calendar.setOnDayClickListener(new OnDayClickListener() {
           @Override
           public void onDayClick(EventDay eventDay) {
               Calendar clickedDayCalendar = eventDay.getCalendar();
               String ggMMAAAA = clickedDayCalendar.get(Calendar.DAY_OF_MONTH)+"/"+
                       ((clickedDayCalendar.get(Calendar.MONTH))+1)+"/"+clickedDayCalendar.get(Calendar.YEAR);
               ggMMAAAA = deleteUselessZero(ggMMAAAA); // day selected
               List<Pasto> pastoListOfTheDay = null;


               try {
                   pastoListOfTheDay = pastoDAO.getPastiOfDate(ggMMAAAA);// carico i pasti della giornata
               } catch (JSONException e) {
                   e.printStackTrace();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               } catch (ExecutionException e) {
                   e.printStackTrace();
               }

               queryEventi.clear();

               if (pastoListOfTheDay != null && ! pastoListOfTheDay.isEmpty()){
                   for (Pasto p : pastoListOfTheDay)
                   {

                       Diet dieta = new Diet();
                       try {
                           if(dietaDAO.getDietById(p.getDietaId())!= null)
                               dieta = dietaDAO.getDietById(p.getDietaId());
                       } catch (JSONException e) {
                           e.printStackTrace();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       } catch (ExecutionException e) {
                           e.printStackTrace();
                       }
                       try {
                           if(animaleDAO.getAnimalById(dieta.getAnimalId())!= null) {
                               String nomeAnimale = animaleDAO.getAnimalById(dieta.getAnimalId()).getName();
                               queryEventi.add(nomeAnimale+" -> "+p.getName() + ": " + p.getQcroccantini()
                                       + "g croccantini " + p.getQumido() + "g umido");
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       } catch (ExecutionException e) {
                           e.printStackTrace();
                       }
                   }
               }

               //metto gli elementi nell'array e li carica sulla lista
               eventi = new String[queryEventi.size()];
               eventi = queryEventi.toArray(eventi);
               adapter = new ArrayAdapter<String>(v.getContext(),R.layout.activity_listview,eventi);
               listaEventi.setAdapter(adapter);
           }
       });



        return v;
    }

    private String deleteUselessZero(String data)
    {
        String rebuiltedString = "";
        StringTokenizer st = new StringTokenizer(data,"/");
        while (st.hasMoreTokens())
        {
            String n = st.nextToken();
            rebuiltedString += Integer.parseInt(n)+"/";
        }
       rebuiltedString = rebuiltedString.substring(0,rebuiltedString.length()-1);

        return rebuiltedString;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            itemDetailFragmentListener = (MainActivityToolbarListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("$context must implement MainActivityToolbarListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.itemDetailFragmentListener.changeTitle(getString(R.string.calendar));
    }
}