package edu.colorado.cires.cruisepack.ui.controller;

import edu.colorado.cires.cruisepack.ui.model.AbstractModel;
import edu.colorado.cires.cruisepack.ui.view.BaseViewPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractController implements PropertyChangeListener {

  private final List<BaseViewPanel> registeredViews = new ArrayList<>();
  private final List<BaseViewPanel> viewsToAdd = new ArrayList<>();
  private final List<BaseViewPanel> viewsToRemove = new ArrayList<>();
  private final Map<Class<? extends AbstractModel>, AbstractModel> registeredModels = new HashMap<>();

  private boolean changing = false;

  public <T extends AbstractModel> void addModel(Class<T> cls, T model) {
    registeredModels.put(cls, model);
    model.addChangeListener(this);
  }

  public <T extends AbstractModel> T getModel(Class<T> cls) {
    return (T) registeredModels.get(cls);
  }

  public <T extends AbstractModel> void removeModel(Class<T> cls) {
    AbstractModel model = registeredModels.remove(cls);
    if (model != null) {
      model.removeChangeListener(this);
    }
  }

  public synchronized void addView(BaseViewPanel view) {
    if (changing) {
      viewsToAdd.add(view);
    } else {
      registeredViews.add(view);
    }
  }

  public synchronized void removeView(BaseViewPanel view) {
    if (changing) {
      viewsToRemove.add(view);
    } else {
      registeredViews.remove(view);
    }
  }


  @Override
  public synchronized void propertyChange(PropertyChangeEvent evt) {
    changing = true;
    try {
      for (BaseViewPanel view: registeredViews) {
        view.onChange(evt);
      }
      registeredViews.removeAll(viewsToRemove);
      registeredViews.addAll(viewsToAdd);
      viewsToRemove.clear();
      viewsToAdd.clear();
    } finally {
      changing = false;
    }

  }

//  /**
//   * This is a convenience method that subclasses can call upon
//   * to fire property changes back to the models. This method
//   * uses reflection to inspect each of the model classes
//   * to determine whether it is the owner of the property
//   * in question. If it isn't, a NoSuchMethodException is thrown,
//   * which the method ignores.
//   *
//   * @param propertyName = The name of the property.
//   * @param newValue = An object that represents the new value
//   * of the property.
//   */
//  protected void setModelProperty(String propertyName, Object newValue) {
//
//    for (AbstractModel model: registeredModels) {
//      try {
//        Method method = model.getClass().getMethod("set"+propertyName, newValue.getClass());
//        method.invoke(model, newValue);
//      } catch (Exception ex) {
//        //  Handle exception.
//      }
//    }
//  }
}
