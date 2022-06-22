//
//  RequestSender.swift
//  RoomMe
//
//  Created by Student2 on 25/05/2022.
//

import Foundation

struct RequestSender {
    var address = "http://3694-95-155-99-184.ngrok.io"
    
    func getRequest(path: String) {
        
    }
    
    func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
}
