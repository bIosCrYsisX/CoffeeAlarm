package com.dalpiazsolutions.coffeealarm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabData.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabData extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textTemp;
    private TextView textHumidity;
    private TextView textVoltage;
    private TextView textTempOut;
    private TextView textHumidOut;
    private ImageView iconWeather;
    private MainController mainController;

    private OnFragmentInteractionListener mListener;

    public TabData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabData.
     */
    // TODO: Rename and change types and number of parameters
    public static TabData newInstance(String param1, String param2) {
        TabData fragment = new TabData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmentdata, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainController = new MainController(getContext());

        textTemp = view.findViewById(R.id.textTemp);
        textHumidity = view.findViewById(R.id.textHumidity);
        textVoltage = view.findViewById(R.id.textVoltage);
        textTempOut = view.findViewById(R.id.textTempOut);
        textHumidOut = view.findViewById(R.id.textHumidOut);

        iconWeather = view.findViewById(R.id.iconWeather);

        float inValues[] = mainController.getInsideWeather();
        textTemp.setText(String.format(Locale.getDefault(), getString(R.string.temp), inValues[0]));
        textHumidity.setText(String.format(Locale.getDefault(), getString(R.string.humidity), inValues[1]));
        textVoltage.setText(String.format(Locale.getDefault(), getString(R.string.voltage), inValues[2]));

        double outValues[] = mainController.getOutsideWeather();

        textTempOut.setText(String.format(Locale.getDefault(), getString(R.string.temp), outValues[0]));
        textHumidOut.setText(String.format(Locale.getDefault(), getString(R.string.humidity), outValues[1]));

        iconWeather.setImageBitmap(mainController.getIcon());

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
