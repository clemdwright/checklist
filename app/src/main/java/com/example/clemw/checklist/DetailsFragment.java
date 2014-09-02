package com.example.clemw.checklist;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class DetailsFragment extends Fragment {
    int placeIndex;
    Place place;
    TextView placeNameView;
    TextView ratingView;
    TextView priceLevelView;
    ToggleButton beenButton;
    Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    // Need to initialize variables in onActivityCreated because onCreate happens at the same time as the Main Activity's onCreate
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // communicator is pointing to the Main Activity
        communicator = (Communicator) getActivity();

        placeNameView = (TextView) getActivity().findViewById(R.id.place_name);
        ratingView = (TextView) getActivity().findViewById(R.id.rating);
        priceLevelView = (TextView) getActivity().findViewById(R.id.price_level);
        beenButton = (ToggleButton) getActivity().findViewById(R.id.been);

        beenButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    communicator.passBeenMarkerState(true, placeIndex);
                } else {
                    communicator.passBeenMarkerState(false, placeIndex);
                }
            }
        });
    }

    public void setPlace(Place place, int placeIndex) {
        this.placeIndex = placeIndex;
        this.place = place;
        placeNameView.setText(place.getName());

        Double rating = place.getRating();
        Double price_level = place.getPriceLevel();
        if (rating != null) ratingView.setText(rating.toString());
        if (price_level != null) priceLevelView.setText(price_level.toString());
    }

    public void changeText(String data) {
        placeNameView.setText(place.getName());
    }
}


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link DetailsFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link DetailsFragment#newInstance} factory method to
// * create an instance of this fragment.
// *
// */
//public class DetailsFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment DetailsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static DetailsFragment newInstance(String param1, String param2) {
//        DetailsFragment fragment = new DetailsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    public DetailsFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_details, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }
//
//}
