package assignments

import com.jfoenix.controls.{JFXSpinner, JFXTextField}
import jfxscala.FxSButton
import rtcollection.immutable.CosineSimilarity
import webscraping.Scrape._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalafx.application.{JFXApp, Platform}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.GridPane

import scala.collection.JavaConversions._
import scalafx.scene.text.{FontWeight, Font}

/**
  * Created by rtorres on 2/9/2016.
  */
object WebScrape extends JFXApp {

  val inputField = new JFXTextField("Enter site here")
  val resultLabel = new Label("  Results : ")
  resultLabel.setFont(Font("Roboto", FontWeight.Bold, 14))

  resultLabel.setPrefWidth(300)
  inputField.setPrefWidth(300)

  val button = new FxSButton("Enter to search") {
    onClick(bestMatch())
    getStyleClass.add("button-raised")
  }

  val spinner = new JFXSpinner

  val mainGrid = new GridPane()

  mainGrid.add(inputField, 0, 0, 2, 1)
  mainGrid.add(button, 2, 0, 1, 1)
  mainGrid.add(resultLabel, 0, 1)
  mainGrid.setPadding(Insets(50, 50, 50, 50)); //margins around the whole grid



  def bestMatch(): Unit = {
    mainGrid.add(spinner, 0, 2)
    val futureMatch: Future[(String, Double)] = Future {
      val userInput = inputField.textProperty().get()
      val desiredFreq = textFrequency(userInput)

      val like = Sites.map((url) => (url, CosineSimilarity.cosineSimilarity(desiredFreq, textFrequency(url)))).sortWith(_._2 < _._2).last
      like
    }

    futureMatch.onComplete {
      case Failure(_) => Platform runLater {
        mainGrid.children -= spinner
        resultLabel.text.set("Invalid URL entered")
      }
      case Success(urlFreqPair) => Platform runLater {
        mainGrid.children -= spinner
        resultLabel.text.set(urlFreqPair._1)
      }
    }

  }

  stage = new JFXApp.PrimaryStage {
    title.value = "Frequency Map"
    width = 700
    height = 500
    scene = new Scene {
      content = mainGrid
      stylesheets = List(getClass.getResource("/jfoenix-components.css").toExternalForm)
    }
  }


}
