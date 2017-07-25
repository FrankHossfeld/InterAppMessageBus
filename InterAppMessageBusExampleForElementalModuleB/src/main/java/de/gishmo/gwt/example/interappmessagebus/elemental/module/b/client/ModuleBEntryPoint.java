package de.gishmo.gwt.example.interappmessagebus.elemental.module.b.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.gishmo.gwt.interappmessagebus.client.InterAppMessage;
import de.gishmo.gwt.interappmessagebus.client.elemental.InterAppMessageBus;
import de.gishmo.gwt.interappmessagebus.client.elemental.prototype.InterAppMessageHandler;

import elemental.client.Browser;
import elemental.html.Window;

public class ModuleBEntryPoint
  implements EntryPoint {

  private FlowPanel protocolContainer;
  private TextBox    postValue;

  /*
   * (non-Javadoc)
   *
   * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
   */
  @Override
  public void onModuleLoad() {
    Window parentWindow = Browser.getWindow()
                                 .getParent();

    Resources        resources = GWT.create(Resources.class);
    ApplicaitonStyle style     = resources.style();
    style.ensureInjected();

    FlowPanel container = new FlowPanel();

    Label headline = new Label("Module B");
    headline.addStyleName(style.headline());
    container.add(headline);

    FlowPanel formularContianer = new FlowPanel();
    formularContianer.addStyleName(style.formularContainer());
    container.add(formularContianer);

    VerticalPanel vp = new VerticalPanel();
    formularContianer.add(vp);

    HorizontalPanel hp01 = new HorizontalPanel();
    vp.add(hp01);
    Label label = new Label("Parmeter:");
    label.addStyleName(style.label());
    hp01.add(label);

    postValue = new TextBox();
    postValue.addStyleName(style.textBox());
    hp01.add(postValue);

    HorizontalPanel hp02 = new HorizontalPanel();
    vp.add(hp02);

    Button showDocIdbutton = new Button("Send Value");
    showDocIdbutton.addStyleName(style.formularButton());
    showDocIdbutton.addClickHandler(event -> {
      InterAppMessageBus.postMessage(parentWindow,
                                     InterAppMessage.builder()
                                                    .source("modeulB")
                                                    .target("modulA")
                                                    .eventType("sendValue")
                                                    .add(postValue.getText())
                                                    .build());
      protocolContainer.add(new Label("Fire Event: >>send<< for Value: >>" + postValue.getText() + "<<"));
    });
    hp02.add(showDocIdbutton);

    protocolContainer = new FlowPanel();
    protocolContainer.addStyleName(style.protocolContainer());
    container.add(protocolContainer);

    InterAppMessageBus.addListener(new InterAppMessageHandler() {
      @Override
      public String getEventType() {
        return "showDocument";
      }

      @Override
      public void onEvent(InterAppMessage event) {
        logEvent(event.getEventType(),
                 event.getParameters()
                      .get(0));
      }
    });

    InterAppMessageBus.addListener(new InterAppMessageHandler() {
      @Override
      public String getEventType() {
        return "editDocument";
      }

      @Override
      public void onEvent(InterAppMessage event) {
        logEvent(event.getEventType(),
                 event.getParameters()
                      .get(0));
      }
    });

    InterAppMessageBus.addListener(new InterAppMessageHandler() {
      @Override
      public String getEventType() {
        return "removeDocument";
      }

      @Override
      public void onEvent(InterAppMessage event) {
        logEvent(event.getEventType(),
                 event.getParameters()
                      .get(0));
      }
    });

    RootPanel.get()
             .add(container);
  }

  private void logEvent(String type,
                        String docId) {
    protocolContainer.add(new Label("capture Event: >>" + type + "<< for DocId: >>" + docId + "<<"));
  }

  public interface Resources
    extends ClientBundle {

    @Source("application.css")
    ApplicaitonStyle style();

  }

  public interface ApplicaitonStyle
    extends CssResource {

    String headline();

    String formularContainer();

    String label();

    String textBox();

    String formularButton();

    String protocolContainer();

  }
}
