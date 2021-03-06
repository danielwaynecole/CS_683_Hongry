package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecStart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecStart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecStart extends Fragment {


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecStart.
     */
    //
    public static RecStart newInstance() {
        return new RecStart();
    }

    public RecStart() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_rec_start, container, false);
        // details button click interface with activity
        final Button eatInSearchStartButton = (Button) inf.findViewById(R.id.newAdventureButton);
        eatInSearchStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eatInSearchStart();
            }
        });
        final Button eatInFavoritesStartButton = (Button) inf.findViewById(R.id.favoritesButton);
        eatInFavoritesStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eatInFavoritesStart();
            }
        });
        return inf;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void eatInSearchStart(){
        mListener.onFragmentInteraction("recNew", null);
    }

    public void eatInFavoritesStart(){
        mListener.onFragmentInteraction("recFaves", null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String s, String d);
    }

}
