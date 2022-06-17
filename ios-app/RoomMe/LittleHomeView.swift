//
//  LittleHomeView.swift
//  RoomMe
//
//  Created by Student2 on 18/05/2022.
//

import SwiftUI

struct Task1: Identifiable{
    var id = UUID()
    var description: String
    var address: String
}

struct TaskRow1: View {
    var task: Task1
    var body: some View {
        VStack {
        HStack{
            Image(systemName: "location.circle")
                .foregroundColor(.gray)
                .imageScale(.large)
                Text("\(task.description)")
            .foregroundColor(.gray)
            .font(.headline)
        }
            Text("\(task.address)")
        }
    }
}

var first1 = Task1(description: "Wioska smerfow", address: "stumilowy las 419")

struct LittleHomeView: View {
    @Binding var sceneNumber: Int
    @State var tasks = [first1]
    var body: some View {
        VStack{
            HStack {
            List(tasks) {
                task in TaskRow1(task: task)
            }}
        NavigationView {
            //Spacer()
            
            VStack{
                Spacer()
                HStack {
                    
                    
                    NavigationLink(destination: NewFlatView(tasks: $tasks), label: {
                Text("CREATE NEW FLAT")
                    .padding()
                    .background(Color.green)
                     .foregroundColor(Color.white)
                     .cornerRadius(10)
            }
            )
                }
            }
            }
        }
    }
}

