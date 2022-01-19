//
//  Screen2ViewController.swift
//  Segue3
//
//  Created by IIUJ on 03/11/2021.
//

import UIKit

class Screen2ViewController: UIViewController {
    
    @IBOutlet weak var display2: UILabel!
    
    @IBOutlet weak var slider2: UISlider!
    var _navigationController: MyNavigationController!
    let range = 1000
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        _navigationController = navigationController as? MyNavigationController
        let _globaData = _navigationController.globalData
        
    }
    
    @IBAction func sliderValueDidChange(_ sender: UISlider) {
        let _globaData = _navigationController.globalData
        display2.text = String (_globaData)
        _navigationController.globalData = Int (sender.value * Float(range))
        
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
