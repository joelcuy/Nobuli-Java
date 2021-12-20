package com.example.nobulijava.adapters;
//
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.core.view.GestureDetectorCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
//
//    OnItemLongClickListener itemLongClickListener;
//    GestureDetectorCompat gestureDetector;
//
//    public RecyclerItemClickListener(RecyclerView recyclerView, OnItemLongClickListener listener) {
//        this.itemLongClickListener = listener;
//        gestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public void onLongPress(MotionEvent e) {
//            }
//        });
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//        View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
//        if (childView != null && itemLongClickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
//            itemLongClickListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
//            return true;
//        }
//        return false;
//    }
//
//
//    public interface OnItemLongClickListener {
//        void onItemLongClick(View view, int position);
//    }
//}
