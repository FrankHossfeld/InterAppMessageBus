package de.gishmo.gwt.interappmessagebus.client.elemental2;

import com.google.gwt.core.client.Scheduler;
import de.gishmo.gwt.interappmessagebus.client.InterAppMessage;
import elemental2.dom.DomGlobal;
import elemental2.dom.Event;
import elemental2.dom.MessageEvent;
import elemental2.dom.Window;
import jsinterop.base.Js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterAppMessageBus {
  
  private static Map<String, List<InterAppMessageHandler>> handlersByType = new HashMap<>();
  
  public static void addListener(InterAppMessageHandler handler) {
    String eventType = handler.getEventType();
    if (eventType == null) {
      return;
    }
    List<InterAppMessageHandler> list = handlersByType.computeIfAbsent(eventType,
                                                                       k -> new ArrayList<>());
    list.add(handler);
    DomGlobal.window.addEventListener(isIE() ? "onmmessage" : "message",
                                      event -> handleEvent(eventType,
                                                           event),
                                      false);
  }
  
  private static boolean isIE() {
    return DomGlobal.navigator.userAgent.indexOf("MSIE ") > 0;
  }
  
  private static void handleEvent(String eventType,
                                  Event event) {
    MessageEvent    messageEvent         = Js.cast(event);
    InterAppMessage interAppMessageEvent = InterAppMessage.parseJson((String) messageEvent.data);
    if (eventType.equals(interAppMessageEvent.getEventType())) {
      final List<InterAppMessageHandler> handlerList = handlersByType.get(interAppMessageEvent.getEventType());
      if (handlerList != null) {
        Scheduler.get()
                 .scheduleDeferred(() -> {
                   for (InterAppMessageHandler eventHandler : handlerList) {
                     eventHandler.onEvent(interAppMessageEvent);
                   }
                 });
      }
    }
  }
  
  public static void postMessage(Window targetWindow,
                                 InterAppMessage event) {
    targetWindow.postMessage(event.toJson(),
                             "*");
  }
  
}
