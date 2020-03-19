package de.gishmo.gwt.example.interappmessagebus.elemental.module.a.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;
import de.gishmo.gwt.interappmessagebus.client.InterAppMessage;
import de.gishmo.gwt.interappmessagebus.client.elemental2.InterAppMessageBus;
import de.gishmo.gwt.interappmessagebus.client.elemental2.InterAppMessageHandler;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import elemental2.dom.HTMLIFrameElement;

public class ModuleAEntryPoint
    implements EntryPoint {
  
  private FlowPanel protocolContainer;
  private TextBox   tbDocId;
  
  @Override
  public void onModuleLoad() {
    Element           frameElement01 = DomGlobal.document.getElementById("moduleBWindow");
    HTMLIFrameElement frameElement   = (HTMLIFrameElement) frameElement01;
    if (frameElement == null) {
      throw new NullPointerException("iFrameElement with id >>moduleBWindow<< not Found");
    }
    
    Resources        resources = GWT.create(Resources.class);
    ApplicaitonStyle style     = resources.style();
    style.ensureInjected();
    
    FlowPanel container = new FlowPanel();
    
    Label headline = new Label("Module A");
    headline.addStyleName(style.headline());
    container.add(headline);
    
    FlowPanel formularContianer = new FlowPanel();
    formularContianer.addStyleName(style.formularContainer());
    container.add(formularContianer);
    
    VerticalPanel vp = new VerticalPanel();
    formularContianer.add(vp);
    
    HorizontalPanel hp01 = new HorizontalPanel();
    vp.add(hp01);
    Label label = new Label("DocId:");
    label.addStyleName(style.label());
    hp01.add(label);
    
    tbDocId = new TextBox();
    tbDocId.addStyleName(style.textBox());
    hp01.add(tbDocId);
    
    HorizontalPanel hp02 = new HorizontalPanel();
    vp.add(hp02);
    
    Button showDocIdbutton = new Button("Send DocID");
    showDocIdbutton.addStyleName(style.formularButton());
    showDocIdbutton.addClickHandler(event -> {
      // Workaround for the missing contentWindow-Property:
      InterAppMessageBus.postMessage(frameElement.contentWindow,
                                     InterAppMessage.builder()
                                                    .source("modeulA")
                                                    .target("modulB")
                                                    .eventType("showDocument")
                                                    .add(tbDocId.getText())
                                                    .build());
      protocolContainer.add(new Label("Fire Event: >>showDocument<< for DocId: >>" + tbDocId.getText() + "<<"));
    });
    hp02.add(showDocIdbutton);
    
    Button editDocIdbutton = new Button("Edit DocID");
    editDocIdbutton.addStyleName(style.formularButton());
    editDocIdbutton.addClickHandler(event -> {
      InterAppMessageBus.postMessage(frameElement.contentWindow,
                                     InterAppMessage.builder()
                                                    .source("modeulA")
                                                    .target("modulB")
                                                    .eventType("editDocument")
                                                    .add(tbDocId.getText())
                                                    .build());
      protocolContainer.add(new Label("Fire Event: >>editDocument<< for DocId: >>" + tbDocId.getText() + "<<"));
    });
    hp02.add(editDocIdbutton);
    
    Button removeDocIdbutton = new Button("Remove DocId");
    removeDocIdbutton.addStyleName(style.formularButton());
    removeDocIdbutton.addClickHandler(event -> {
      InterAppMessageBus.postMessage(frameElement.contentWindow,
                                     InterAppMessage.builder()
                                                    .source("modeulA")
                                                    .target("modulB")
                                                    .eventType("removeDocument")
                                                    .add(tbDocId.getText())
                                                    .build());
      protocolContainer.add(new Label("Fire Event: >>removeDocument<< for DocId: >>" + tbDocId.getText() + "<<"));
    });
    hp02.add(removeDocIdbutton);
    
    protocolContainer = new FlowPanel();
    protocolContainer.addStyleName(style.protocolContainer());
    container.add(protocolContainer);
    
    InterAppMessageBus.addListener(new InterAppMessageHandler() {
      
      @Override
      public String getEventType() {
        return "sendValue";
      }
      
      @Override
      public void onEvent(InterAppMessage event) {
        logEvent(event.getEventType(),
                 event.getParameters()
                      .get(0));
      }
    });
    
    RootPanel.get("moduleA")
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
