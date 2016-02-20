import com.jfoenix.controls.JFXSlider.IndicatorPosition
import com.jfoenix.controls.{JFXSlider, JFXRadioButton}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.ToggleGroup
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

/**
  * Hello world for JFoenix.
  */
object HelloFoenix extends JFXApp {
  val group : ToggleGroup = new ToggleGroup()

  val javaRadio : JFXRadioButton = new JFXRadioButton("JavaFX")
  javaRadio.setPadding(Insets(10))
  javaRadio.setToggleGroup(group)
  val jfxRadio = new JFXRadioButton("JFoenix")
  jfxRadio.setPadding(Insets(10))
  jfxRadio.setToggleGroup(group)

  val hor_left = new JFXSlider()
  hor_left.setMinWidth(300)
  hor_left.setIndicatorPosition(IndicatorPosition.RIGHT)

  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 500
    height = 500
    scene = new Scene {
      fill = Color.Grey
      val box = new VBox(100, hor_left, jfxRadio, javaRadio)
      box.setPadding(Insets(5,5,5,50))
      content =  box
    }
  }
}
