package observer;

import model.NationalParkGraph;

public interface NationalParkObserver {
        void onModelChanged(NationalParkGraph graph);
}
