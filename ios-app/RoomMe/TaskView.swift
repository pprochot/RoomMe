//
//  TaskView.swift
//  RoomMe
//
//  Created by Student2 on 18/05/2022.
//

import SwiftUI

struct Task: Identifiable {
    var id = UUID()
    var description: String
}

struct TaskRow: View {
    var task: Task
    var body: some View {
        HStack{
            Image(systemName: "largecircle.fill.circle")
                .foregroundColor(.gray)
                .imageScale(.large)
                Text("\(task.description)")
            .foregroundColor(.gray)
            .font(.headline)
        }
    }
}

var first = Task(description: "grzybobranie")
var second = Task(description: "rosol cooking")
//var tasks = [first, second]
struct TaskView: View {
    @Binding var sceneNumber: Int
    @State var tasks = [first, second]
    var body: some View {
        VStack{
            HStack {
            List(tasks) {
                task in TaskRow(task: task)
            }}
        NavigationView {
            //Spacer()
            
            VStack{
                Spacer()
                HStack {
                    
                    /*Button(action: {
                                   withAnimation(.easeOut(duration: 0.3)) {
                                       self.sceneNumber = 1
                                   }
                               }) {
                                   Text("CREATE NEW HOUSEWORK")
                               }
                    
                               .padding()
                               .frame(width: 150, height: 100)
                                .cornerRadius(40)*/
                    
                    NavigationLink(destination: NewHouseworkView(tasks: $tasks), label: {
                Text("CREATE NEW HOUSEWORK")
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

