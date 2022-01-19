//
//  MyNavigationController.swift
//  Segue3
//
//  Created by IIUJ on 03/11/2021.
//

import UIKit

class MyNavigationController: UINavigationController {
    
    
    var globalData = 5 {
        didSet {
            if globalData > 1000 {
                globalData = oldValue
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        if let dVC = segue.destination as? Screen1ViewController {
            dVC.local1Data = globalData
        }
        
    }
    

}
