//
//  RequestSender.swift
//  RoomMe
//
//  Created by Student2 on 25/05/2022.
//

import Foundation

struct RequestSender {
    var address = "http://3694-95-155-99-184.ngrok.io"
    /*
    func postRequest(path: String, body: Dictionary<String, Any>) -> String {
        let url = URL(string: "\(address)\(path)")!
        //var request = URLRequest(url: url)
        var request = URLRequest(url: url)
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        

        request.httpBody = try? JSONSerialization.data(withJSONObject: body)
        request.httpMethod = "POST"
        let session = URLSession(configuration: .default)
        var result = ""
        let task = session.dataTask(with: request) { data, response, error in
            guard let data = data else { return }
            result = String(data: data, encoding: .utf8)!
        }
        task.resume() // <- otherwise your network request won't be started
        return result
        /*
        var result = ""
        NSURLConnection.sendAsynchronousRequest(request, queue: OperationQueue.main) {(response, data, error) in
            guard let data = data else { return }
            result = String(data: data, encoding: .utf8)!
        }
        
        let session = URLSession.shared
        let a = convertToDictionary(text: result)
        let task = session.dataTask(with: url) { data, response, error in
            
        if let error = error {
            print("POST Request Error: \(error.localizedDescription)")
            return
        }
            
        guard let httpResponse = response as? HTTPURLResponse,
              (200...299).contains(httpResponse.statusCode) else {
                  print("Invalid Response received from the server")
                  return
              }
        
        guard let responseData = data else {
            print("nil Data received from the server")
            return
        }
        var result: Dictionary<String, Any>
        do {
            if let jsonResponse = try JSONSerialization.jsonObject(with: responseData, options: .mutableContainers) as? [String: Any] {
                NSLog(String(jsonResponse))
                result = jsonResponse
            } else {
                print("data maybe corrupted or in wrong format")
                throw URLError(.badServerResponse)
            }
        } catch let error {
            print("JSON Parsing Error: \(error.localizedDescription)")
        }
        }
    
        task.resume()
        return result*/
    }*/
    
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
