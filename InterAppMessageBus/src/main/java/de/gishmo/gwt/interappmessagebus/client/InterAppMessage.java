package de.gishmo.gwt.interappmessagebus.client;

import elemental.js.json.JsJsonObject;
import elemental.json.Json;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class InterAppMessage {

  private final static String KEY_SOURCE               = "source";
  private final static String KEY_TARGET               = "target";
  private final static String KEY_EVENT_TYPE           = "eventType";
  private final static String KEY_NUMBER_OF_PAREMETERS = "numberOfParameters";
  private final static String KEY_PARAMETER            = "parameters";

  private String       source;
  private String       target;
  private String       eventType;
  private List<String> parameters;

  @SuppressWarnings("unused")
  private InterAppMessage() {
  }

  private InterAppMessage(Builder builder) {
    assert builder.source != null : "source has no value!";
    assert builder.target != null : "target has no value!";
    assert builder.eventType != null : "eventType has no value!";

    this.source = builder.source;
    this.target = builder.target;
    this.eventType = builder.eventType;
    this.parameters = builder.parameters;
  }

  /**
   * Erzeugt aus einem Json-String eine Instanz eines InterAppMessageEvent
   *
   * @param jsonString JSON-String eines InterAppMessageEvents
   * @return Instanz des InterAppMesssageEventmit den Daten des JSON-Strings
   */
  public static InterAppMessage parseJson(String jsonString) {
    JsonObject jsonObject = Json.instance()
                                .parse(jsonString);
    int parameterCount;
    try {
      parameterCount = Integer.parseInt(jsonObject.get(InterAppMessage.KEY_NUMBER_OF_PAREMETERS));
    } catch (NumberFormatException e) {
      parameterCount = 0;
    }
    InterAppMessage.Builder builder = InterAppMessage.builder()
                                                     .source(jsonObject.get(InterAppMessage.KEY_SOURCE))
                                                     .target(jsonObject.get(InterAppMessage.KEY_TARGET))
                                                     .eventType(jsonObject.get(InterAppMessage.KEY_EVENT_TYPE));
    for (int i = 0; i < parameterCount; i++) {
      builder.add(jsonObject.get(InterAppMessage.KEY_PARAMETER + Integer.toString(i)));
    }
    return builder.build();
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * URL der Source (aktuell nur zur Info)
   *
   * @return URL der Source
   */
  public String getSource() {
    return source;
  }

  /**
   * URL des Targets (aktuell nur zur Info)
   *
   * @return URL des Targets
   */
  public String getTarget() {
    return target;
  }

  /**
   * Name des Events (required)
   *
   * @return Name des Events
   */
  public String getEventType() {
    return eventType;
  }

  /**
   * Liste der Parameter (Liste of Strings)
   *
   * @return Liste mit Parametern
   */
  public List<String> getParameters() {
    return parameters;
  }

  /**
   * Wandelt den Event in einen Json-String um
   *
   * @return Json_string des Events
   */
  public String toJson() {
    JsonObject jsonObject = JsJsonObject.create();
    jsonObject.put(InterAppMessage.KEY_SOURCE,
                   this.source);
    jsonObject.put(InterAppMessage.KEY_TARGET,
                   this.target);
    jsonObject.put(InterAppMessage.KEY_EVENT_TYPE,
                   this.eventType);
    jsonObject.put(InterAppMessage.KEY_NUMBER_OF_PAREMETERS,
                   Integer.toString(parameters.size()));
    for (int i = 0; i < parameters.size(); i++) {
      jsonObject.put(InterAppMessage.KEY_PARAMETER + Integer.toString(i),
                     parameters.get(i));
    }
    return jsonObject.toJson();
  }

  public static final class Builder {
    String source;
    String target;
    String eventType;
    List<String> parameters = new ArrayList<>();

    /**
     * URL der Source (aktuell nur zur Info)
     *
     * @param source URL der Source
     * @return InterAppMesssageBus Builder
     */
    public Builder source(String source) {
      this.source = source;
      return this;
    }

    /**
     * URL des Targets (aktuell nur zur Info)
     *
     * @param target URL des Targets
     * @return InterAppMesssageBus Builder
     */
    public Builder target(String target) {
      this.target = target;
      return this;
    }

    /**
     * Name des Events (required)
     *
     * @param eventType Name des Events
     * @return InterAppMesssageBus Builder
     */
    public Builder eventType(String eventType) {
      this.eventType = eventType;
      return this;
    }

    /**
     * Fügt einen String als Parameter zum Event hinzu. Es können 0 - n Parameter vom Typ String mitgegeben werdem.
     *
     * @param parameter Paramete des Events
     * @return InterAppMesssageBus Builder
     */
    public Builder add(String parameter) {
      parameters.add(parameter);
      return this;
    }

    /**
     * Erzeugt den Event mit den übergebenen Daten
     *
     * @return Instanz des InterAppMesssageEvent
     */
    public InterAppMessage build() {
      return new InterAppMessage(this);
    }
  }
}
