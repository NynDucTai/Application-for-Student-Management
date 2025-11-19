package com.example.doanlaptrinhhuongdoituong.model;

import com.example.doanlaptrinhhuongdoituong.view.HomeViewFactory;

public class MainViewModel {
    private static MainViewModel mainViewModel;
    private final HomeViewFactory viewFactory;

    private MainViewModel() {
        this.viewFactory = new HomeViewFactory();
    }

    public static synchronized MainViewModel getInstance() {
        if (mainViewModel == null) {
            mainViewModel = new MainViewModel();
        }
        return mainViewModel;
    }

    public HomeViewFactory getViewFactory() {
        return viewFactory;
    }
}