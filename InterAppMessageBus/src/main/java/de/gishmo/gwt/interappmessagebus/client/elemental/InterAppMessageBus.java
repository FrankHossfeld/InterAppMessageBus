package de.gishmo.gwt.interappmessagebus.client.elemental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;

import de.gishmo.gwt.interappmessagebus.client.InterAppMessage;
import de.gishmo.gwt.interappmessagebus.client.elemental.prototype.InterAppMessageHandler;

import elemental.client.Browser;
import elemental.events.Event;
import elemental.events.MessageEvent;
import elemental.html.Window;

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
    Browser.getWindow()
           .addEventListener(isIE() ? "onmmessage" : "message",
                             event -> handleEvent(eventType,
                                                  event));
  }

  private static boolean isIE() {
    String userAgent = Browser.getWindow()
                              .getNavigator()
                              .getUserAgent();
    return userAgent.indexOf("MSIE ") > 0;
  }

  private static void handleEvent(String eventType,
                                  Event event) {
    MessageEvent    messageEvent         = (MessageEvent) event;
    InterAppMessage interAppMessageEvent = InterAppMessage.parseJson((String) messageEvent.getData());
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
