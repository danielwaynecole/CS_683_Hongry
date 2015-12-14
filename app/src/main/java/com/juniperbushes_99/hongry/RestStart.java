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
 * {@link RestStart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestStart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestStart extends Fragment {

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RestStart.
     */
    public static RestStart newInstance() {
        RestStart fragment = new RestStart();
        return fragment;
    }

    public RestStart() {
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
        View inf = inflater.inflate(R.layout.fragment_rest_start, container, false);
        final Button eatInSearchStartButton = (Button) inf.findViewById(R.id.newAdventureButton);
        eatInSearchStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eatOutSearchStart();
            }
        });
        final Button eatInFavoritesStartButton = (Button) inf.findViewById(R.id.favoritesButton);
        eatInFavoritesStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eatOutFavoritesStart();
            }
        });
        return inf;
    }

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

    public void eatOutSearchStart(){
        mListener.onFragmentInteraction("restNew", null);
    }

    public void eatOutFavoritesStart(){
        mListener.onFragmentInteraction("restFaves", null);

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
        public void onFragmentInteraction(String s, String d);
    }

}
