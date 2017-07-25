package de.gishmo.gwt.interappmessagebus.client.elemental.prototype;

import de.gishmo.gwt.interappmessagebus.client.InterAppMessage;

public interface InterAppMessageHandler {

  String getEventType();

  void onEvent(InterAppMessage event);

}
