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
 * {@link HungryHome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HungryHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HungryHome extends Fragment {

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HungryHome.
     */
    public static HungryHome newInstance() {
        HungryHome fragment = new HungryHome();
        return fragment;
    }

    public HungryHome() {
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
        View inf =  inflater.inflate(R.layout.fragment_hungry_home, container, false);
        // details button click interface with activity
        final Button eatInButton = (Button) inf.findViewById(R.id.eatIn);
        eatInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eatInStart();
            }
        });
        final Button eatOutButton = (Button) inf.findViewById(R.id.eatOut);
        eatOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eatOutStart();
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

    private void eatInStart(){
        mListener.onFragmentInteraction("eatInStart", null);
    }

    private void eatOutStart(){
        mListener.onFragmentInteraction("eatOutStart", null);
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
