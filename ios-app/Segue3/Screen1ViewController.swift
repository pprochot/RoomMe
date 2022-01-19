//
//  ViewController.swift
//  Segue3
//
//  Created by IIUJ on 03/11/2021.
//
import SwiftUI
import UIKit

class Screen1ViewController: UIViewController {
    
    
      
    var _navigationController: MyNavigationController!
    
    @IBOutlet weak var textField1: UITextField!
  
    var local1Data = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        _navigationController = navigationController as? MyNavigationController
        
    }
    @IBAction func textField1DidChange(_ sender: UITextField) {
        
        if let text = sender.text, let data = Int(text){
            
            if data <= 1000{
                _navigationController.globalData = data
                sender.backgroundColor = UIColor.white
            }
            else {
                sender.backgroundColor = UIColor.red
            }
            
            
        }
        
    }
    
    @IBAction func backgroundTapped() {
        
        view.endEditing(true)
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        //textField1.text = String (_navigationController.globalData)
    }
    
}

